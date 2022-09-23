/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesState
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.currencies.update
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.oztechan.ccc.client.model.Currency as ClientCurrency
import com.oztechan.ccc.common.model.Currency as CommonCurrency

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val subject: CurrenciesViewModel by lazy {
        CurrenciesViewModel(settingsDataSource, currencyDataSource, adRepository, analyticsManager)
    }

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val commonCurrency = CommonCurrency("EUR", "Euro", "€", isActive = true)
    private val clientCurrency = commonCurrency.toUIModel()

    private val currencyListCommon = listOf(commonCurrency)
    private val currencyListClient = listOf(clientCurrency)

    private val currencyListFlow = flowOf(currencyListCommon)

    @BeforeTest
    override fun setup() {
        super.setup()

        given(currencyDataSource)
            .invocation { collectAllCurrencies() }
            .thenReturn(currencyListFlow)

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        given(settingsDataSource)
            .invocation { currentBase }
            .thenReturn(clientCurrency.name)
    }

    // Analytics
    @Test
    fun if_user_properties_set_correct() {
        subject // init
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(currencyListCommon.count().toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation {
                setUserProperty(
                    UserProperty.ActiveCurrencies(currencyListCommon.joinToString(",") { currency -> currency.name })
                )
            }
            .wasInvoked()
    }

    // Analytics
    @Test
    fun user_properties_should_not_set_if_there_is_no_active_currency() {
        val nonActiveCurrencyList = listOf(CommonCurrency("EUR", "Euro", "€", isActive = false))

        given(currencyDataSource)
            .invocation { collectAllCurrencies() }
            .thenReturn(flowOf(nonActiveCurrencyList))

        subject // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(nonActiveCurrencyList.count().toString())) }
            .wasNotInvoked()

        verify(analyticsManager)
            .invocation {
                setUserProperty(
                    UserProperty.ActiveCurrencies(nonActiveCurrencyList.joinToString(",") { currency -> currency.name })
                )
            }
            .wasNotInvoked()
    }

    // SEED
    @Test
    fun states_updates_correctly() {
        val state = MutableStateFlow(CurrenciesState())

        state.before {
            state.update(
                currencyList = currencyListClient,
                loading = true,
                selectionVisibility = true
            )
        }.after {
            assertNotNull(it)
            assertEquals(currencyListClient, it.currencyList)
            assertTrue { it.loading }
            assertTrue { it.selectionVisibility }
        }
    }

    // init
    @Test
    fun init_updates_states_correctly() = runTest {
        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currencyListClient, it.currencyList)
            assertFalse { it.selectionVisibility }
            assertEquals(currencyListClient.toMutableList(), subject.data.unFilteredList)
        }
    }

    @Test
    fun `show FewCurrency effect if there is less active currency than MINIMUM_ACTIVE_CURRENCY and not firstRun`() {
        runTest {
            given(currencyDataSource)
                .invocation { collectAllCurrencies() }
                .thenReturn(
                    flow {
                        delay(SECOND)
                        emit(currencyListCommon)
                    }
                )
        }

        subject.effect.before {
            // init
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
        }
    }

    // public methods
    @Test
    fun hideSelectionVisibility() = subject.state.before {
        subject.hideSelectionVisibility()
    }.after {
        assertNotNull(it)
        assertFalse { it.selectionVisibility }
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, subject.shouldShowBannerAd())

        verify(adRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    @Test
    fun isFirstRun() {
        val mockValue = Random.nextBoolean()
        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(mockValue)

        assertEquals(mockValue, subject.isFirstRun())

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        // runTest can be removed after kotlin move to new memory management
        runTest {
            subject.event.onQueryChange(query)
            assertEquals(query, subject.data.query)
        }
    }

    // Event
    @Test
    fun updateAllCurrenciesState() {
        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        given(settingsDataSource)
            .invocation { currentBase }
            .thenReturn("EUR")

        val mockValue = Random.nextBoolean()
        subject.event.updateAllCurrenciesState(mockValue)

        runTest {
            verify(currencyDataSource)
                .coroutine { updateAllCurrencyState(mockValue) }
                .wasInvoked()
        }
    }

    @Test
    fun onItemClick() {
        subject.event.onItemClick(clientCurrency)

        runTest {
            verify(currencyDataSource)
                .coroutine {
                    updateCurrencyStateByName(
                        clientCurrency.name,
                        !clientCurrency.isActive
                    )
                }.wasInvoked()
        }
    }

    @Test
    fun onQueryChange() = with(subject) {
        val dollar = ClientCurrency("USD", "American Dollar", "$", "1231")

        val originalList = mutableListOf<ClientCurrency>().apply {
            add(clientCurrency)
            add(dollar)
        }

        subject.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("USD")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("USD", subject.data.query)
            assertFalse { it.loading }
        }

        subject.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("Euro")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(clientCurrency) }
            assertEquals("Euro", subject.data.query)
            assertFalse { it.loading }
        }

        subject.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("$")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("$", subject.data.query)
            assertFalse { it.loading }
        }

        subject.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("something")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.isEmpty() }
            assertEquals("something", subject.data.query)
            assertFalse { it.loading }
        }

        subject.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("o")
        }.after {
            assertNotNull(it)
            assertEquals(2, it.currencyList.size)
            assertEquals("o", subject.data.query)
            assertFalse { it.loading }
        }
    }

    @Test
    fun onItemLongClick() {
        val previousValue = subject.state.value.selectionVisibility
        subject.state.before {
            subject.event.onItemLongClick()
        }.after {
            assertNotNull(it)
            assertEquals(!previousValue, it.selectionVisibility)
        }

        subject.state.before {
            subject.event.onItemLongClick()
        }.after {
            assertNotNull(it)
            assertEquals(previousValue, it.selectionVisibility)
        }
    }

    @Test
    fun onCloseClick() {
        subject.effect.before {
            subject.onCloseClick()
        }.after {
            assertIs<CurrenciesEffect.Back>(it)
            assertEquals("", subject.data.query)
        }

        // show selections
        subject.event.onItemLongClick()

        subject.state.before {
            subject.onCloseClick()
        }.after {
            assertNotNull(it)
            assertFalse { it.selectionVisibility }
            assertEquals("", subject.data.query)
        }
    }

    @Test
    fun onDoneClick() {
        // where there is single currency
        val dollar = ClientCurrency("USD", "American Dollar", "$", "123", isActive = true)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { subject.data.query.isEmpty() }
        }

        // where there are 2 active currencies
        subject.data.unFilteredList = mutableListOf(clientCurrency, dollar)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.OpenCalculator>(it)
            assertTrue { subject.data.query.isEmpty() }

            verify(settingsDataSource)
                .invocation { firstRun = false }
                .wasInvoked()
        }

        // where there are 2 currencies but only 1 active
        val dollarNotActive = ClientCurrency("USD", "American Dollar", "$", "123", isActive = false)
        subject.data.unFilteredList = mutableListOf(clientCurrency, dollarNotActive)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertEquals(true, subject.data.query.isEmpty())
        }
    }
}

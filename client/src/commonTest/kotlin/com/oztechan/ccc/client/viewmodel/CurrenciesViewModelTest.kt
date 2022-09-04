/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesState
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.currencies.update
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
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

@Suppress("TooManyFunctions")
class CurrenciesViewModelTest : BaseViewModelTest() {

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val viewModel: CurrenciesViewModel by lazy {
        CurrenciesViewModel(settingsDataSource, currencyDataSource, adRepository, analyticsManager)
    }

    private val commonCurrency = CommonCurrency("EUR", "Euro", "€", isActive = true)
    private val clientCurrency = commonCurrency.toUIModel()

    private val currencyListCommon = listOf(commonCurrency)
    private val currencyListClient = listOf(clientCurrency)

    private val currencyListFlow = flowOf(currencyListCommon)

    @BeforeTest
    fun setup() {
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
        viewModel // init
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

        viewModel // init

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
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currencyListClient, it.currencyList)
            assertFalse { it.selectionVisibility }
            assertEquals(currencyListClient.toMutableList(), viewModel.data.unFilteredList)
        }
    }

    // public methods
    @Test
    fun hideSelectionVisibility() = viewModel.state.before {
        viewModel.hideSelectionVisibility()
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

        assertEquals(mockBoolean, viewModel.shouldShowBannerAd())

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

        assertEquals(mockValue, viewModel.isFirstRun())

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        // runTest can be removed after kotlin move to new memory management
        runTest {
            viewModel.event.onQueryChange(query)
            assertEquals(query, viewModel.data.query)
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
        viewModel.event.updateAllCurrenciesState(mockValue)

        runTest {
            verify(currencyDataSource)
                .coroutine { updateAllCurrencyState(mockValue) }
                .wasInvoked()
        }
    }

    @Test
    fun onItemClick() {
        viewModel.event.onItemClick(clientCurrency)

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
    fun onQueryChange() = with(viewModel) {
        val dollar = ClientCurrency("USD", "American Dollar", "$", "1231")

        val originalList = mutableListOf<ClientCurrency>().apply {
            add(clientCurrency)
            add(dollar)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("USD")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("USD", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("Euro")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(clientCurrency) }
            assertEquals("Euro", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("$")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("$", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("something")
        }.after {
            assertNotNull(it)
            assertTrue { it.currencyList.isEmpty() }
            assertEquals("something", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("o")
        }.after {
            assertNotNull(it)
            assertEquals(2, it.currencyList.size)
            assertEquals("o", viewModel.data.query)
            assertFalse { it.loading }
        }
    }

    @Test
    fun onItemLongClick() {
        val previousValue = viewModel.state.value.selectionVisibility
        viewModel.state.before {
            viewModel.event.onItemLongClick()
        }.after {
            assertNotNull(it)
            assertEquals(!previousValue, it.selectionVisibility)
        }

        viewModel.state.before {
            viewModel.event.onItemLongClick()
        }.after {
            assertNotNull(it)
            assertEquals(previousValue, it.selectionVisibility)
        }
    }

    @Test
    fun onCloseClick() {
        viewModel.effect.before {
            viewModel.onCloseClick()
        }.after {
            assertIs<CurrenciesEffect.Back>(it)
            assertEquals("", viewModel.data.query)
        }

        // show selections
        viewModel.event.onItemLongClick()

        viewModel.state.before {
            viewModel.onCloseClick()
        }.after {
            assertNotNull(it)
            assertFalse { it.selectionVisibility }
            assertEquals("", viewModel.data.query)
        }
    }

    @Test
    fun onDoneClick() {
        // where there is single currency
        val dollar = ClientCurrency("USD", "American Dollar", "$", "123", isActive = true)

        viewModel.effect.before {
            viewModel.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { viewModel.data.query.isEmpty() }
        }

        // where there are 2 active currencies
        viewModel.data.unFilteredList = mutableListOf(clientCurrency, dollar)

        viewModel.effect.before {
            viewModel.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.OpenCalculator>(it)
            assertTrue { viewModel.data.query.isEmpty() }

            verify(settingsDataSource)
                .invocation { firstRun = false }
                .wasInvoked()
        }

        // where there are 2 currencies but only 1 active
        val dollarNotActive = ClientCurrency("USD", "American Dollar", "$", "123", isActive = false)
        viewModel.data.unFilteredList = mutableListOf(clientCurrency, dollarNotActive)

        viewModel.effect.before {
            viewModel.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertEquals(true, viewModel.data.query.isEmpty())
        }
    }
}

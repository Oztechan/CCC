/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.currencies

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.constants.SECOND
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class CurrenciesViewModelTest {

    private val viewModel: CurrenciesViewModel by lazy {
        CurrenciesViewModel(appStorage, calculationStorage, currencyDataSource, adControlRepository, analyticsManager)
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @Mock
    private val calculationStorage = mock(classOf<CalculationStorage>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private var currency1 = Currency("EUR", "Euro", "€", isActive = true)
    private val currency2 = Currency("USD", "Dollar", "$", isActive = true)
    private val currency3 = Currency("TRY", "Turkish Lira", "₺", isActive = true)

    private val currencyList = listOf(currency1, currency2)

    private val currencyListFlow = flowOf(currencyList)

    private var dollar = Currency("USD", "American Dollar", "$", 1231.0)

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        Dispatchers.setMain(UnconfinedTestDispatcher())

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(currencyListFlow)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)
    }

    // Analytics
    @Test
    fun `if user properties set correct`() {
        viewModel // init
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(currencyList.count().toString())) }
            .wasInvoked()
    }

    // Analytics
    @Test
    fun `user properties should not set if there is no active currency`() {
        val nonActiveCurrencyList = listOf(Currency("EUR", "Euro", "€", isActive = false))

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(flowOf(nonActiveCurrencyList))

        viewModel // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(nonActiveCurrencyList.count().toString())) }
            .wasNotInvoked()
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currencyList, it.currencyList)
            assertFalse { it.selectionVisibility }
            assertEquals(currencyList.toMutableList(), viewModel.data.unFilteredList)
        }
    }

    @Test
    fun `show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY and not firstRun`() = runTest {
        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(
                flow {
                    delay(SECOND)
                    emit(listOf(currency1))
                }
            )

        viewModel.effect.firstOrNull().let {
            assertIs<CurrenciesEffect.FewCurrency>(it)
        }
    }

    @Test
    fun `don't show FewCurrency effect if there is MINIMUM_ACTIVE_CURRENCY and not firstRun`() = runTest {
        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn("") // in order to get ChangeBase effect, have to have an effect to finish test

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(
                flow {
                    delay(SECOND)
                    emit(listOf(currency1, currency1, currency1))
                }
            )

        viewModel.effect.firstOrNull().let {
            assertIs<CurrenciesEffect.ChangeBase>(it)
        }
    }

    @Test
    fun `don't show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY it is firstRun`() = runTest {
        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn("") // in order to get ChangeBase effect, have to have an effect to finish test

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(
                flow {
                    delay(SECOND)
                    emit(listOf(currency1))
                }
            )

        viewModel.effect.firstOrNull().let {
            assertIs<CurrenciesEffect.ChangeBase>(it)
        }
    }

    // public methods
    @Test
    fun hideSelectionVisibility() = runTest {
        viewModel.state.onSubscription {
            viewModel.hideSelectionVisibility()
        }.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.selectionVisibility }
        }
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBoolean = Random.nextBoolean()

        given(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, viewModel.shouldShowBannerAd())

        verify(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    @Test
    fun isFirstRun() {
        val mockValue = Random.nextBoolean()
        given(appStorage)
            .invocation { firstRun }
            .thenReturn(mockValue)

        assertEquals(mockValue, viewModel.isFirstRun())

        verify(appStorage)
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

    @Test
    fun `verifyCurrentBase should set first active currency base when currentBase is empty`() = runTest {
        val firstActiveBase = currency1.code // first active currency

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(
                flow {
                    delay(SECOND)
                    emit(currencyList)
                }
            )

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn("")

        viewModel.effect.firstOrNull().let {
            assertIs<CurrenciesEffect.ChangeBase>(it)
            assertEquals(firstActiveBase, it.newBase)
        }

        verify(calculationStorage)
            .invocation { currentBase = firstActiveBase }
            .wasInvoked()
    }

    @Test
    fun `verifyCurrentBase should set first active currency base when currentBase is unset`() = runTest {
        currency1 = currency1.copy(isActive = false) // make first item in list not active

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(
                flow {
                    delay(SECOND)
                    emit(listOf(currency1, currency2, currency3))
                }
            )

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code) // not active one

        viewModel.effect.firstOrNull().let {
            assertIs<CurrenciesEffect.ChangeBase>(it)
            assertEquals(currency2.code, it.newBase)
        }

        verify(calculationStorage)
            .invocation { currentBase = currency2.code }
            .wasInvoked()
    }

    // Event
    @Test
    fun updateAllCurrenciesState() {
        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn("EUR")

        val mockValue = Random.nextBoolean()
        viewModel.event.updateAllCurrenciesState(mockValue)

        runTest {
            verify(currencyDataSource)
                .coroutine { updateCurrencyStates(mockValue) }
                .wasInvoked()
        }
    }

    @Test
    fun onItemClick() {
        viewModel.event.onItemClick(currency1)

        runTest {
            verify(currencyDataSource)
                .coroutine {
                    updateCurrencyStateByCode(
                        currency1.code,
                        !currency1.isActive
                    )
                }.wasInvoked()
        }
    }

    @Test
    fun onQueryChange() = runTest {
        val originalList = mutableListOf<Currency>().apply {
            add(currency1)
            add(dollar)
        }

        viewModel.state.onSubscription {
            viewModel.data.unFilteredList = originalList
            viewModel.event.onQueryChange("USD")
        }.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("USD", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.onSubscription {
            viewModel.data.unFilteredList = originalList
            viewModel.event.onQueryChange("Euro")
        }.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(currency1) }
            assertEquals("Euro", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.onSubscription {
            viewModel.data.unFilteredList = originalList
            viewModel.event.onQueryChange("$")
        }.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.currencyList.contains(dollar) }
            assertEquals("$", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.onSubscription {
            viewModel.data.unFilteredList = originalList
            viewModel.event.onQueryChange("something")
        }.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.currencyList.isEmpty() }
            assertEquals("something", viewModel.data.query)
            assertFalse { it.loading }
        }

        viewModel.state.onSubscription {
            viewModel.data.unFilteredList = originalList
            viewModel.event.onQueryChange("o")
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(2, it.currencyList.size)
            assertEquals("o", viewModel.data.query)
            assertFalse { it.loading }
        }
    }

    @Test
    fun onItemLongClick() = runTest {
        val previousValue = viewModel.state.value.selectionVisibility
        viewModel.state.onSubscription {
            viewModel.event.onItemLongClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(!previousValue, it.selectionVisibility)
        }

        viewModel.state.onSubscription {
            viewModel.event.onItemLongClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(previousValue, it.selectionVisibility)
        }
    }

    @Test
    fun onCloseClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.onCloseClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.Back>(it)
            assertEquals("", viewModel.data.query)
        }

        // show selections
        viewModel.event.onItemLongClick()

        viewModel.state.onSubscription {
            viewModel.onCloseClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.selectionVisibility }
            assertEquals("", viewModel.data.query)
        }
    }

    @Test
    fun onDoneClick() = runTest {
        // where there is single currency
        val dollarActive = dollar.copy(isActive = true)

        viewModel.data.unFilteredList = mutableListOf(currency1)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { viewModel.data.query.isEmpty() }
        }

        // where there are 2 active currencies
        viewModel.data.unFilteredList = mutableListOf(currency1, dollarActive)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.OpenCalculator>(it)
            assertTrue { viewModel.data.query.isEmpty() }

            verify(appStorage)
                .invocation { firstRun = false }
                .wasInvoked()
        }

        // where there are 2 currencies but only 1 active
        val dollarNotActive = dollar.copy(isActive = false)
        viewModel.data.unFilteredList = mutableListOf(currency1, dollarNotActive)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertEquals(true, viewModel.data.query.isEmpty())
        }
    }
}

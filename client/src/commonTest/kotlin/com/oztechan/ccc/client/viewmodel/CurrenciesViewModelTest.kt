/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.helper.BaseViewModelTest
import com.oztechan.ccc.client.helper.util.after
import com.oztechan.ccc.client.helper.util.before
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.common.core.infrastructure.constants.SECOND
import com.oztechan.ccc.common.core.model.Currency
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.delay
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

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val subject: CurrenciesViewModel by lazy {
        CurrenciesViewModel(appStorage, calculatorStorage, currencyDataSource, adControlRepository, analyticsManager)
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @Mock
    private val calculatorStorage = mock(classOf<CalculatorStorage>())

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
    override fun setup() {
        super.setup()

        given(currencyDataSource)
            .invocation { getCurrenciesFlow() }
            .thenReturn(currencyListFlow)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)
    }

    // Analytics
    @Test
    fun `if user properties set correct`() {
        subject // init
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

        subject // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(nonActiveCurrencyList.count().toString())) }
            .wasNotInvoked()
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currencyList, it.currencyList)
            assertFalse { it.selectionVisibility }
            assertEquals(currencyList.toMutableList(), subject.data.unFilteredList)
        }
    }

    @Test
    fun `show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY and not firstRun`() {
        runTest {
            given(currencyDataSource)
                .invocation { getCurrenciesFlow() }
                .thenReturn(
                    flow {
                        delay(SECOND)
                        emit(listOf(currency1))
                    }
                )
        }

        subject.effect.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
        }
    }

    @Test
    fun `don't show FewCurrency effect if there is MINIMUM_ACTIVE_CURRENCY and not firstRun`() {
        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn("") // in order to get ChangeBase effect, have to have an effect to finish test

        runTest {
            given(currencyDataSource)
                .invocation { getCurrenciesFlow() }
                .thenReturn(
                    flow {
                        delay(SECOND)
                        emit(listOf(currency1, currency1, currency1))
                    }
                )
        }

        subject.effect.after {
            assertIs<CurrenciesEffect.ChangeBase>(it)
        }
    }

    @Test
    fun `don't show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY it is firstRun`() {
        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn("") // in order to get ChangeBase effect, have to have an effect to finish test

        runTest {
            given(currencyDataSource)
                .invocation { getCurrenciesFlow() }
                .thenReturn(
                    flow {
                        delay(SECOND)
                        emit(listOf(currency1))
                    }
                )
        }

        subject.effect.after {
            assertIs<CurrenciesEffect.ChangeBase>(it)
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

        given(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, subject.shouldShowBannerAd())

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

        assertEquals(mockValue, subject.isFirstRun())

        verify(appStorage)
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

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn("")

        subject.effect.after {
            assertIs<CurrenciesEffect.ChangeBase>(it)
            assertEquals(firstActiveBase, it.newBase)
        }

        verify(calculatorStorage)
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

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code) // not active one

        subject.effect.after {
            assertIs<CurrenciesEffect.ChangeBase>(it)
            assertEquals(currency2.code, it.newBase)
        }

        verify(calculatorStorage)
            .invocation { currentBase = currency2.code }
            .wasInvoked()
    }

    // Event
    @Test
    fun updateAllCurrenciesState() {
        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn("EUR")

        val mockValue = Random.nextBoolean()
        subject.event.updateAllCurrenciesState(mockValue)

        runTest {
            verify(currencyDataSource)
                .coroutine { updateCurrencyStates(mockValue) }
                .wasInvoked()
        }
    }

    @Test
    fun onItemClick() {
        subject.event.onItemClick(currency1)

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
    fun onQueryChange() = with(subject) {
        val originalList = mutableListOf<Currency>().apply {
            add(currency1)
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
            assertTrue { it.currencyList.contains(currency1) }
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
        val dollarActive = dollar.copy(isActive = true)

        subject.data.unFilteredList = mutableListOf(currency1)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { subject.data.query.isEmpty() }
        }

        // where there are 2 active currencies
        subject.data.unFilteredList = mutableListOf(currency1, dollarActive)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.OpenCalculator>(it)
            assertTrue { subject.data.query.isEmpty() }

            verify(appStorage)
                .invocation { firstRun = false }
                .wasInvoked()
        }

        // where there are 2 currencies but only 1 active
        val dollarNotActive = dollar.copy(isActive = false)
        subject.data.unFilteredList = mutableListOf(currency1, dollarNotActive)

        subject.effect.before {
            subject.onDoneClick()
        }.after {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertEquals(true, subject.data.query.isEmpty())
        }
    }
}

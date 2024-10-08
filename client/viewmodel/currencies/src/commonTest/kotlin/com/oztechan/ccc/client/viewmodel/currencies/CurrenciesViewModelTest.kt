/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.currencies

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Currency
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
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
import kotlin.time.Duration.Companion.seconds

internal class CurrenciesViewModelTest {

    private val viewModel: CurrenciesViewModel by lazy {
        CurrenciesViewModel(
            appStorage,
            calculationStorage,
            currencyDataSource,
            adControlRepository,
            analyticsManager
        )
    }

    private val appStorage = mock<AppStorage>(MockMode.autoUnit)

    private val calculationStorage = mock<CalculationStorage>(MockMode.autoUnit)

    private val currencyDataSource = mock<CurrencyDataSource>(MockMode.autoUnit)

    private val adControlRepository = mock<AdControlRepository>()

    private val analyticsManager = mock<AnalyticsManager>(MockMode.autoUnit)

    private var currency1 = Currency("EUR", "Euro", "€", isActive = true)
    private val currency2 = Currency("USD", "Dollar", "$", isActive = true)
    private val currency3 = Currency("TRY", "Turkish Lira", "₺", isActive = true)

    private val currencyList = listOf(currency1, currency2)

    private val currencyListFlow = flowOf(currencyList)

    private var dollar = Currency("USD", "American Dollar", "$", "1231")
    private val shouldShowAds = Random.nextBoolean()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { currencyDataSource.getCurrenciesFlow() }
            .returns(currencyListFlow)

        every { appStorage.firstRun }
            .returns(false)

        every { calculationStorage.currentBase }
            .returns(currency1.code)

        every { adControlRepository.shouldShowBannerAd() }
            .returns(shouldShowAds)
    }

    // Analytics
    @Test
    fun `if user properties set correct`() {
        viewModel // init
        verify {
            analyticsManager.setUserProperty(
                UserProperty.CurrencyCount(
                    currencyList.count().toString()
                )
            )
        }
    }

    // Analytics
    @Test
    fun `user properties should not set if there is no active currency`() {
        val nonActiveCurrencyList = listOf(Currency("EUR", "Euro", "€", isActive = false))

        every { currencyDataSource.getCurrenciesFlow() }
            .returns(flowOf(nonActiveCurrencyList))

        viewModel // init

        verify(VerifyMode.not) {
            analyticsManager.setUserProperty(
                UserProperty.CurrencyCount(
                    nonActiveCurrencyList.count().toString()
                )
            )
        }
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currencyList, it.currencyList)
            assertFalse { it.selectionVisibility }
            assertFalse { it.isOnboardingVisible } // mocked false
            assertEquals(currencyList.toMutableList(), viewModel.data.unFilteredList)
            assertEquals(shouldShowAds, it.isBannerAdVisible)
            assertFalse { it.loading }
        }

        verify { adControlRepository.shouldShowBannerAd() }

        verify { appStorage.firstRun }
    }

    @Test
    fun `init updates data correctly`() {
        assertNotNull(viewModel.data)
        assertEquals(currencyList, viewModel.data.unFilteredList)
        assertTrue { viewModel.data.query.isEmpty() }
    }

    @Test
    fun `show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY and not firstRun`() =
        runTest {
            every { currencyDataSource.getCurrenciesFlow() }
                .returns(
                    flow {
                        delay(1.seconds.inWholeMilliseconds)
                        emit(listOf(currency1))
                    }
                )

            viewModel.effect.firstOrNull().let {
                assertNotNull(it)
                assertIs<CurrenciesEffect.FewCurrency>(it)
            }
        }

    @Test
    fun `don't show FewCurrency effect if there is MINIMUM_ACTIVE_CURRENCY and not firstRun`() =
        runTest {
            every { calculationStorage.currentBase }
                .returns("")

            every { currencyDataSource.getCurrenciesFlow() }
                .returns(
                    flow {
                        emit(listOf(currency1, currency1, currency1))
                    }
                )

            // init
            viewModel

            verify { analyticsManager.trackEvent(Event.BaseChange(Param.Base(currency1.code))) }
            verify { analyticsManager.setUserProperty(UserProperty.BaseCurrency(currency1.code)) }
            verify { calculationStorage.currentBase = currency1.code }
        }

    @Test
    fun `don't show FewCurrency effect if there is less than MINIMUM_ACTIVE_CURRENCY it is firstRun`() =
        runTest {
            every { appStorage.firstRun }
                .returns(true)

            every { calculationStorage.currentBase }
                .returns("")

            every { currencyDataSource.getCurrenciesFlow() }
                .returns(
                    flow {
                        emit(listOf(currency1))
                    }
                )
            // init
            viewModel

            verify { analyticsManager.trackEvent(Event.BaseChange(Param.Base(currency1.code))) }
            verify { analyticsManager.setUserProperty(UserProperty.BaseCurrency(currency1.code)) }
            verify { calculationStorage.currentBase = currency1.code }
        }

    @Test
    fun `query is updated when list is filtered`() {
        val query = "query"
        // runTest can be removed after kotlin move to new memory management
        runTest {
            viewModel.event.onQueryChange(query)
            assertEquals(query, viewModel.data.query)
        }
    }

    @Test
    fun `verifyCurrentBase should set first active currency base when currentBase is empty`() =
        runTest {
            val firstActiveBase = currency1.code // first active currency

            every { currencyDataSource.getCurrenciesFlow() }
                .returns(
                    flow {
                        emit(currencyList)
                    }
                )

            every { calculationStorage.currentBase }
                .returns("")
            // init
            viewModel

            verify { analyticsManager.trackEvent(Event.BaseChange(Param.Base(firstActiveBase))) }
            verify { analyticsManager.setUserProperty(UserProperty.BaseCurrency(firstActiveBase)) }
            verify { calculationStorage.currentBase = firstActiveBase }
        }

    @Test
    fun `verifyCurrentBase should set first active currency base when currentBase is unset`() =
        runTest {
            currency1 = currency1.copy(isActive = false) // make first item in list not active

            every { currencyDataSource.getCurrenciesFlow() }
                .returns(
                    flow {
                        emit(listOf(currency1, currency2, currency3))
                    }
                )

            every { calculationStorage.currentBase }
                .returns(currency1.code) // not active one
            // init
            viewModel

            verify { analyticsManager.trackEvent(Event.BaseChange(Param.Base(currency2.code))) }
            verify { analyticsManager.setUserProperty(UserProperty.BaseCurrency(currency2.code)) }
            verify { calculationStorage.currentBase = currency2.code }
        }

    // Event
    @Test
    fun updateAllCurrenciesState() {
        every { appStorage.firstRun }
            .returns(false)

        every { calculationStorage.currentBase }
            .returns("EUR")

        val mockValue = Random.nextBoolean()
        viewModel.event.updateAllCurrenciesState(mockValue)

        runTest {
            verifySuspend { currencyDataSource.updateCurrencyStates(mockValue) }
        }
    }

    @Test
    fun onItemClick() {
        viewModel.event.onItemClick(currency1)

        runTest {
            verifySuspend {
                currencyDataSource.updateCurrencyStateByCode(currency1.code, !currency1.isActive)
            }
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
            assertNotNull(it)
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
        every { appStorage.firstRun }
            .returns(true)

        // where there is single currency
        val dollarActive = dollar.copy(isActive = true)

        viewModel.data.unFilteredList = mutableListOf(currency1)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { viewModel.data.query.isEmpty() }
            assertTrue { viewModel.state.value.isOnboardingVisible }
        }

        // where there are 2 active currencies
        viewModel.data.unFilteredList = mutableListOf(currency1, dollarActive)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.OpenCalculator>(it)
            assertTrue { viewModel.data.query.isEmpty() }

            assertFalse { viewModel.state.value.isOnboardingVisible }

            verify { appStorage.firstRun = false }
        }

        // where there are 2 currencies but only 1 active
        val dollarNotActive = dollar.copy(isActive = false)
        viewModel.data.unFilteredList = mutableListOf(currency1, dollarNotActive)

        viewModel.effect.onSubscription {
            viewModel.onDoneClick()
        }.firstOrNull().let {
            assertIs<CurrenciesEffect.FewCurrency>(it)
            assertTrue { viewModel.data.query.isEmpty() }
            assertFalse { viewModel.state.value.isOnboardingVisible }
        }
    }
}

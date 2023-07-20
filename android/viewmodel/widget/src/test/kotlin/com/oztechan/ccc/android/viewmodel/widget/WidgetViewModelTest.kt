package com.oztechan.ccc.android.viewmodel.widget

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import io.mockative.Mock
import io.mockative.anything
import io.mockative.classOf
import io.mockative.configure
import io.mockative.eq
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.days

@Suppress("OPT_IN_USAGE")
class WidgetViewModelTest {

    private val viewModel: WidgetViewModel by lazy {
        WidgetViewModel(
            calculationStorage = calculationStorage,
            backendApiService = backendApiService,
            currencyDataSource = currencyDataSource,
            appStorage = appStorage
        )
    }

    @Mock
    private val calculationStorage = configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private val base = "EUR"
    private val firstBase = "USD"
    private val lastBase = "TRY"

    private val activeCurrencyList = listOf(
        Currency(code = firstBase, name = "Dollar", symbol = "$", isActive = true),
        Currency(code = base, name = "Euro", symbol = "€", isActive = true),
        Currency(code = lastBase, name = "Turkish Lira", symbol = "₺", isActive = true)
    )

    private val conversion = Conversion(base = base, eur = 1.111111, usd = 2.222222, `try` = 3.333333)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        Logger.setLogWriters(CommonWriter())

        val mockEndDate = Random.nextLong()

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(mockEndDate)

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(base)

        given(calculationStorage)
            .invocation { precision }
            .thenReturn(3)

        runTest {
            given(backendApiService)
                .coroutine { getConversion(base) }
                .thenReturn(conversion)

            given(currencyDataSource)
                .coroutine { getActiveCurrencies() }
                .thenReturn(activeCurrencyList)
        }
    }

//    @Test
//    fun `ArrayIndexOutOfBoundsException never thrown`() = runTest {
//        // first currency
//        given(calculationStorage)
//            .invocation { currentBase }
//            .thenReturn(firstBase)
//
//        given(backendApiService)
//            .coroutine { getConversion(firstBase) }
//            .thenReturn(conversion)
//
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick()
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(true)
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(false)
//        }
//
//        // middle currency
//        given(calculationStorage)
//            .invocation { currentBase }
//            .thenReturn(base)
//
//        given(backendApiService)
//            .coroutine { getConversion(base) }
//            .thenReturn(conversion)
//
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick()
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(true)
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(false)
//        }
//
//        // last currency
//        given(calculationStorage)
//            .invocation { currentBase }
//            .thenReturn(lastBase)
//
//        given(backendApiService)
//            .coroutine { getConversion(lastBase) }
//            .thenReturn(conversion)
//
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick()
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(true)
//        }
//        repeat(activeCurrencyList.count()) {
//            viewModel.event.onRefreshClick(false)
//        }
//    }

    @Test
    fun `init sets isPremium and currentBase`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(base, it.currentBase)
            assertEquals(appStorage.premiumEndDate.isNotPassed(), it.isPremium)
        }
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        verify(backendApiService)
            .coroutine { getConversion(base) }
            .wasInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun `if user is not premium no api call and db query are not invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        verify(backendApiService)
            .coroutine { getConversion(base) }
            .wasNotInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()
    }

    @Test
    fun `when onRefreshClick called all the conversion rates for currentBase is calculated`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.state.onSubscription {
            viewModel.event.onRefreshClick()
        }.firstOrNull().let {
            assertNotNull(it)
            it.currencyList.forEach { currency ->
                conversion.getRateFromCode(currency.code).let { rate ->
                    assertNotNull(rate)
                    assertEquals(rate.getFormatted(calculationStorage.precision), currency.rate)
                }
            }
        }
    }

    @Test
    fun `when onRefreshClick called with null, base is not updated`() = runTest {
        // to not invoke getFreshWidgetData
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(anything<String>()))
            .wasNotInvoked()
    }

    @Test
    fun onNextClick() = runTest {
        // when onNextClick, base is updated next or the first active currency
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onNextClick()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .invocation { currentBase = lastBase }
            .wasInvoked()

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(lastBase)

        viewModel.event.onNextClick()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .invocation { currentBase = firstBase }
            .wasInvoked()
    }

    @Test
    fun onPreviousClick() = runTest {
        // when onRefreshClick, base is updated previous or the last active currency
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onPreviousClick()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .invocation { currentBase = firstBase }
            .wasInvoked()

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(firstBase)

        viewModel.event.onPreviousClick()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .invocation { currentBase = lastBase }
            .wasInvoked()
    }
}

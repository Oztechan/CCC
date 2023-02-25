package com.oztechan.ccc.android.viewmodel.widget

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
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
    private val fistBase = "USD"
    private val lastBase = "TRY"

    private val activeCurrencyList = listOf(
        Currency(code = fistBase, name = "Dollar", symbol = "$", isActive = true, rate = "1.2"),
        Currency(code = base, name = "Euro", symbol = "€", isActive = true, rate = "2.3"),
        Currency(code = lastBase, name = "Turkish Lira", symbol = "₺", isActive = true, rate = "3.4")
    )

    private val conversion = Conversion(base = base, eur = 1.0, usd = 2.0, `try` = 3.0)

    @BeforeTest
    fun setup() {
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

    @Test
    fun `init sets isPremium and currentBase`() {
        assertEquals(base, viewModel.state.currentBase)
        assertEquals(appStorage.premiumEndDate.isNotPassed(), viewModel.state.isPremium)
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.refreshWidgetData()

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

        viewModel.refreshWidgetData()

        verify(backendApiService)
            .coroutine { getConversion(base) }
            .wasNotInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()
    }

    @Test
    fun `when refreshWidgetData called all the conversion rates for currentBase is calculated`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.refreshWidgetData()

        viewModel.state.currencyList
            .forEach {
                assertEquals(
                    conversion.getRateFromCode(it.code)?.getFormatted(calculationStorage.precision).orEmpty(),
                    it.rate
                )
            }
    }

    @Test
    fun `when refreshWidgetData called with null, base is not updated`() = runTest {
        // to not invoke getFreshWidgetData
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.refreshWidgetData()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(anything<String>()))
            .wasNotInvoked()
    }

    @Test
    fun `when refreshWidgetData called with true, base is updated next or the first active currency`() = runTest {
        // to not invoke getFreshWidgetData
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.refreshWidgetData(true)

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(fistBase))
            .wasInvoked()

        viewModel.refreshWidgetData(true)

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(lastBase))
            .wasInvoked()
    }

    @Test
    fun `when refreshWidgetData called with false, base is updated previous or the last active currency`() = runTest {
        // to not invoke getFreshWidgetData
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.refreshWidgetData(false)

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(lastBase))
            .wasInvoked()

        viewModel.refreshWidgetData(false)

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasInvoked()

        verify(calculationStorage)
            .setter(calculationStorage::currentBase)
            .with(eq(fistBase))
            .wasInvoked()
    }
}

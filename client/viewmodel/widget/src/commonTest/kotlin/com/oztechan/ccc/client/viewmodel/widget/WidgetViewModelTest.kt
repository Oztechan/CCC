package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.ExchangeRate
import io.mockative.Mock
import io.mockative.classOf
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
        WidgetViewModel(calculationStorage, backendApiService, currencyDataSource, appStorage)
    }

    @Mock
    private val calculationStorage = mock(classOf<CalculationStorage>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private val base = "EUR"

    private val currency = Currency(code = base, name = "Euro", symbol = "€")
    private val exchangeRate = ExchangeRate(base = base, conversion = Conversion(base = base))

    @BeforeTest
    fun setup() {
        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(base)
    }

    @Test
    fun `init sets isPremium and currentBase`() {
        val mockEndDate = Random.nextLong()

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(mockEndDate)

        assertEquals(base, viewModel.state.currentBase)
        assertEquals(!appStorage.premiumEndDate.isItOver(), viewModel.state.isPremium)
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        given(backendApiService)
            .coroutine { getConversion(base) }
            .thenReturn(exchangeRate)

        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf(currency))

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
}

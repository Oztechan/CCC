package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.isPremiumExpired
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import com.oztechan.ccc.common.core.infrastructure.util.DAY
import com.oztechan.ccc.common.core.infrastructure.util.nowAsLong
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.ExchangeRate
import com.oztechan.ccc.common.data.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.test.BaseViewModelTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("OPT_IN_USAGE")
class WidgetViewModelTest : BaseViewModelTest<WidgetViewModel>() {

    override val subject: WidgetViewModel by lazy {
        WidgetViewModel(calculatorStorage, backendApiService, currencyDataSource, appStorage)
    }

    @Mock
    private val calculatorStorage = mock(classOf<CalculatorStorage>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private val base = "EUR"

    private val currency = Currency(code = base, name = "Euro", symbol = "€")
    private val exchangeRate = ExchangeRate(base = base, conversion = Conversion(base = base))

    override fun setup() {
        super.setup()

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(base)
    }

    @Test
    fun `init sets isPremium and currentBase`() {
        val mockEndDate = Random.nextLong()

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(mockEndDate)

        assertEquals(base, subject.state.currentBase)
        assertEquals(!appStorage.premiumEndDate.isPremiumExpired(), subject.state.isPremium)
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + DAY)

        given(backendApiService)
            .coroutine { getConversion(base) }
            .thenReturn(exchangeRate)

        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf(currency))

        subject.refreshWidgetData()

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
            .thenReturn(nowAsLong() - DAY)

        subject.refreshWidgetData()

        verify(backendApiService)
            .coroutine { getConversion(base) }
            .wasNotInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()
    }
}

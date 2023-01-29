package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.common.core.infrastructure.constants.DAY
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

@Suppress("OPT_IN_USAGE")
class WidgetViewModelTest {

    private val subject: WidgetViewModel by lazy {
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

    @BeforeTest
    fun setup() {
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
        assertEquals(!appStorage.premiumEndDate.isItOver(), subject.state.isPremium)
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

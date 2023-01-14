package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.isPremiumExpired
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.nowAsLong
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

    private val mockBase = "mock"

    override fun setup() {
        super.setup()

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(mockBase)
    }

    @Test
    fun `init sets isPremium and currentBase`() {
        val mockEndDate = Random.nextLong()

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(mockEndDate)

        assertEquals(mockBase, subject.state.currentBase)
        assertEquals(!appStorage.premiumEndDate.isPremiumExpired(), subject.state.isPremium)
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + DAY)

        subject.refreshWidgetData()

        verify(backendApiService)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()
    }

    @Test
    fun `if user is not premium no api call and db query are not invoked`() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - DAY)

        subject.refreshWidgetData()

        verify(backendApiService)
            .coroutine { getConversion(mockBase) }
            .wasNotInvoked()

        verify(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .wasNotInvoked()
    }
}

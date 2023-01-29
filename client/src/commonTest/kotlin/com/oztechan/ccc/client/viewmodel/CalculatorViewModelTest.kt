/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.helper.BaseViewModelTest
import com.oztechan.ccc.client.helper.util.after
import com.oztechan.ccc.client.helper.util.before
import com.oztechan.ccc.client.model.ConversionState
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.calculateRate
import com.oztechan.ccc.client.util.getConversionStringFromBase
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.util.getFormatted
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.ExchangeRate
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
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

@Suppress("OPT_IN_USAGE", "TooManyFunctions")
internal class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override val subject: CalculatorViewModel by lazy {
        CalculatorViewModel(
            calculatorStorage,
            backendApiService,
            currencyDataSource,
            conversionDataSource,
            adControlRepository,
            analyticsManager
        )
    }

    @Mock
    private val calculatorStorage = mock(classOf<CalculatorStorage>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val conversionDataSource = mock(classOf<ConversionDataSource>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val currency1 = Currency("USD", "Dollar", "$", 12345.678, true)
    private val currency2 = Currency("EUR", "Dollar", "$", 12345.678, true)
    private val currencyList = listOf(currency1, currency2)
    private val exchangeRate = ExchangeRate(currency1.code, null, Conversion())

    @BeforeTest
    override fun setup() {
        super.setup()

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        given(calculatorStorage)
            .invocation { lastInput }
            .thenReturn("")

        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(currencyList))

        given(calculatorStorage)
            .invocation { precision }
            .thenReturn(3)

        runTest {
            given(conversionDataSource)
                .coroutine { getConversionByBase(currency1.code) }
                .thenReturn(exchangeRate.conversion)

            given(backendApiService)
                .coroutine { getConversion(currency1.code) }
                .thenReturn(exchangeRate)

            given(currencyDataSource)
                .coroutine { getCurrencyByCode(currency1.code) }
                .thenReturn(currency1)
        }
    }

    @Test
    fun `conversion should be fetched on init`() = runTest {
        verify(backendApiService)
            .coroutine { getConversion(currency1.code) }
            .wasInvoked()
    }

    @Test
    fun `init sets the latest base and input`() = runTest {
        val mock = "mock"

        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        given(calculatorStorage)
            .invocation { lastInput }
            .thenReturn(mock)

        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency1.code, it.base)
            assertEquals(mock, it.input)
        }
    }

    @Test
    fun `when api fails and there is conversion in db then conversion rates are calculated`() = runTest {
        given(backendApiService)
            .coroutine { getConversion(currency1.code) }
            .thenThrow(Exception())

        subject.state.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertNotNull(it)
            assertFalse { it.loading }
            assertEquals(ConversionState.Offline(exchangeRate.conversion.date), it.conversionState)

            val result = currencyList.onEach { currency ->
                currency.rate = exchangeRate.conversion.calculateRate(currency.code, it.output)
                    .getFormatted(calculatorStorage.precision)
                    .toStandardDigits().toDoubleOrNull() ?: 0.0
            }

            assertEquals(result, it.currencyList)
        }

        verify(conversionDataSource)
            .coroutine { getConversionByBase(currency1.code) }
            .wasInvoked()
    }

    @Test
    fun `when api fails and there is no conversion in db then error state displayed`() = runTest {
        given(backendApiService)
            .coroutine { getConversion(currency1.code) }
            .thenThrow(Exception())

        given(conversionDataSource)
            .coroutine { getConversionByBase(currency1.code) }
            .thenReturn(null)

        subject.effect.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertIs<CalculatorEffect.Error>(it)

            subject.state.value.let { state ->
                assertNotNull(state)
                assertFalse { state.loading }
                assertEquals(ConversionState.Error, state.conversionState)
            }
        }

        verify(conversionDataSource)
            .coroutine { getConversionByBase(currency1.code) }
            .wasInvoked()
    }

    @Test
    fun `when api fails and there is no offline and no enough currency few currency effect emitted`() = runTest {
        given(backendApiService)
            .coroutine { getConversion(currency1.code) }
            .thenThrow(Exception())

        given(conversionDataSource)
            .coroutine { getConversionByBase(currency1.code) }
            .thenReturn(null)

        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(listOf(currency1)))

        subject.effect.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertIs<CalculatorEffect.FewCurrency>(it)

            subject.state.value.let { state ->
                assertNotNull(state)
                assertFalse { state.loading }
                assertEquals(ConversionState.Error, state.conversionState)
            }
        }

        verify(conversionDataSource)
            .coroutine { getConversionByBase(currency1.code) }
            .wasInvoked()
    }

    @Test
    fun `when input is too long it should drop the last digit and give warning`() {
        val fortyFiveDigitNumber = "1234567890+1234567890+1234567890+1234567890+1"
        subject.effect.before {
            subject.event.onKeyPress(fortyFiveDigitNumber)
        }.after {
            assertIs<CalculatorEffect.TooBigNumber>(it)
            assertFalse { subject.state.value.loading }
            assertEquals(fortyFiveDigitNumber.dropLast(1), subject.state.value.input)
        }
    }

    @Test
    fun `when output is too long it should drop the last digit and give warning`() {
        val nineteenDigitNumber = "123 567 901 345 789"
        subject.effect.before {
            subject.event.onKeyPress(nineteenDigitNumber)
        }.after {
            assertIs<CalculatorEffect.TooBigNumber>(it)
            assertFalse { subject.state.value.loading }
            assertEquals(nineteenDigitNumber.dropLast(1), subject.state.value.input)
        }
    }

    @Test
    fun `calculate output should return formatted finite output or empty string`() {
        subject.state.before {
            subject.event.onKeyPress("1/0")
        }.after {
            assertNotNull(it)
            assertEquals("", it.output)
        }
        subject.state.before {
            subject.event.onKeyPress("AC") // clean input
            subject.event.onKeyPress("1/1")
        }.after {
            assertNotNull(it)
            assertEquals("1", it.output)
        }
        subject.state.before {
            subject.event.onKeyPress("AC") // clean input
            subject.event.onKeyPress("1111/1")
        }.after {
            assertNotNull(it)
            assertEquals("1 111", it.output)
        }
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        subject // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(currencyList.count().toString())) }
            .wasInvoked()
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

    // Event
    @Test
    fun onBarClick() = subject.effect.before {
        subject.event.onBarClick()
    }.after {
        assertIs<CalculatorEffect.OpenBar>(it)
    }

    @Test
    fun onSettingsClicked() = subject.effect.before {
        subject.event.onSettingsClicked()
    }.after {
        assertIs<CalculatorEffect.OpenSettings>(it)
    }

    // todo need to refactor
//    @Test
//    fun onItemClick() {
//        var currency = currency1
//        subject.state.before {
//            subject.event.onItemClick(currency1)
//        }.after {
//            assertNotNull(it)
//            assertEquals(currency1.code, it.base)
//            assertEquals(currency1.rate, it.input)
//        }
//
//        // when last digit is . it should be removed
//        currency = currency.copy(rate = "123.")
//
//        subject.state.before {
//            subject.event.onItemClick(currency)
//        }.after {
//            assertNotNull(it)
//            assertEquals(currency.code, it.base)
//            assertEquals("123", it.input)
//        }
//
//        currency = currency.copy(rate = "123 456.78")
//
//        subject.state.before {
//            subject.event.onItemClick(currency)
//        }.after {
//            assertNotNull(it)
//            assertEquals(currency.code, it.base)
//            assertEquals("123456.78", it.input)
//        }
//    }

    @Test
    fun onItemImageLongClick() = subject.effect.before {
        subject.event.onItemImageLongClick(currency1)
    }.after {
        assertIs<CalculatorEffect.ShowConversion>(it)
        assertEquals(
            currency1.getConversionStringFromBase(
                subject.state.value.base,
                subject.data.conversion
            ),
            it.text
        )
        assertEquals(currency1.code, it.code)

        verify(analyticsManager)
            .invocation { trackEvent(Event.ShowConversion(Param.Base(currency1.code))) }
            .wasInvoked()
    }

    @Test
    fun onItemAmountLongClick() = subject.effect.before {
        subject.event.onItemAmountLongClick(currency1.rate.toString())
    }.after {
        assertEquals(
            CalculatorEffect.CopyToClipboard(currency1.rate.toString()),
            it
        )

        verify(analyticsManager)
            .invocation { trackEvent(Event.CopyClipboard) }
            .wasInvoked()
    }

    @Test
    fun onKeyPress() = with(subject) {
        val key = "1"

        // emits when input is empty
        state.before {
            event.onKeyPress(key)
        }.after {
            assertNotNull(it)
            assertEquals(key, it.input)
        }

        // adds when input is not empty
        state.before {
            event.onKeyPress(key)
        }.after {
            assertNotNull(it)
            assertEquals(key + key, it.input)
        }

        // resets to "" when AC clicked
        state.before {
            event.onKeyPress(KEY_AC)
        }.after {
            assertNotNull(it)
            assertEquals("", it.input)
        }

        // does nothing when press DEL if input is already empty
        state.before {
            event.onKeyPress(KEY_DEL)
        }.after {
            assertNotNull(it)
            assertEquals("", it.input)
        }

        // Deletes last digit when press DEL
        state.before {
            event.onKeyPress(key)
            event.onKeyPress(key)
            event.onKeyPress(KEY_DEL)
        }.after {
            assertNotNull(it)
            assertEquals(key, it.input)
        }
    }

    @Test
    fun onBaseChanged() {
        given(calculatorStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        runTest {
            given(backendApiService)
                .coroutine { getConversion(currency1.code) }
                .thenReturn(exchangeRate)
        }

        subject.state.before {
            subject.event.onBaseChange(currency1.code)
        }.after {
            assertNotNull(it)
            assertNotNull(subject.data.conversion)
            assertEquals(currency1.code, subject.data.conversion!!.base)
            assertEquals(currency1.code, it.base)

            verify(analyticsManager)
                .invocation { trackEvent(Event.BaseChange(Param.Base(currency1.code))) }
                .wasInvoked()

            verify(analyticsManager)
                .invocation { setUserProperty(UserProperty.BaseCurrency(currency1.code)) }
                .wasInvoked()
        }
    }
}

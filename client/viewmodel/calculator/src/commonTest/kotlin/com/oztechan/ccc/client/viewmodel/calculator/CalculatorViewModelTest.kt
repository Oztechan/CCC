/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.calculator

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.toStandardDigits
import com.oztechan.ccc.client.core.shared.util.toSupportedCharacters
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.oztechan.ccc.client.viewmodel.calculator.model.ConversionState
import com.oztechan.ccc.client.viewmodel.calculator.util.calculateRate
import com.oztechan.ccc.client.viewmodel.calculator.util.getConversionStringFromBase
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
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

@Suppress("OPT_IN_USAGE", "TooManyFunctions")
internal class CalculatorViewModelTest {

    private val viewModel: CalculatorViewModel by lazy {
        CalculatorViewModel(
            calculationStorage,
            backendApiService,
            currencyDataSource,
            conversionDataSource,
            adControlRepository,
            analyticsManager
        )
    }

    @Mock
    private val calculationStorage = configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val conversionDataSource = configure(mock(classOf<ConversionDataSource>())) { stubsUnitByDefault = true }

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager = configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

    private val currency1 = Currency("USD", "Dollar", "$", "12345.678", true)
    private val currency2 = Currency("EUR", "Dollar", "$", "12345.678", true)
    private val currencyList = listOf(currency1, currency2)
    private val conversion = Conversion(currency1.code, "12.12.2121")

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        Dispatchers.setMain(UnconfinedTestDispatcher())

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        given(calculationStorage)
            .invocation { lastInput }
            .thenReturn("")

        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(currencyList))

        given(calculationStorage)
            .invocation { precision }
            .thenReturn(3)

        runTest {
            given(conversionDataSource)
                .coroutine { getConversionByBase(currency1.code) }
                .thenReturn(conversion)

            given(backendApiService)
                .coroutine { getConversion(currency1.code) }
                .thenReturn(conversion)

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

        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        given(calculationStorage)
            .invocation { lastInput }
            .thenReturn(mock)

        viewModel.state.firstOrNull().let {
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

        viewModel.state.onSubscription {
            viewModel.event.onKeyPress("1") // trigger api call
        }.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
            assertEquals(ConversionState.Offline(conversion.date), it.conversionState)

            val result = currencyList.onEach { currency ->
                currency.rate = conversion.calculateRate(currency.code, it.output)
                    .getFormatted(calculationStorage.precision)
                    .toStandardDigits()
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

        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress("1") // trigger api call
        }.firstOrNull().let {
            assertIs<CalculatorEffect.Error>(it)

            viewModel.state.value.let { state ->
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

        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress("1") // trigger api call
        }.firstOrNull().let {
            assertIs<CalculatorEffect.FewCurrency>(it)

            viewModel.state.value.let { state ->
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
    fun `when input is too long it should drop the last digit and give warning`() = runTest {
        val fortyFiveDigitNumber = "1234567890+1234567890+1234567890+1234567890+1"
        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress(fortyFiveDigitNumber)
        }.firstOrNull().let {
            assertIs<CalculatorEffect.TooBigNumber>(it)
            assertFalse { viewModel.state.value.loading }
            assertEquals(fortyFiveDigitNumber.dropLast(1), viewModel.state.value.input)
        }
    }

    @Test
    fun `when output is too long it should drop the last digit and give warning`() = runTest {
        val nineteenDigitNumber = "123 567 901 345 789"
        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress(nineteenDigitNumber)
        }.firstOrNull().let {
            assertIs<CalculatorEffect.TooBigNumber>(it)
            assertFalse { viewModel.state.value.loading }
            assertEquals(nineteenDigitNumber.dropLast(1), viewModel.state.value.input)
        }
    }

    @Test
    fun `calculate output should return formatted finite output or empty string`() = runTest {
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress("1/0")
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals("", it.output)
        }
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress("AC") // clean input
            viewModel.event.onKeyPress("1/1")
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals("1", it.output)
        }
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress("AC") // clean input
            viewModel.event.onKeyPress("1111/1")
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals("1 111", it.output)
        }
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

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

        assertEquals(mockBoolean, viewModel.shouldShowBannerAd())

        verify(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    // Event
    @Test
    fun onBarClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onBarClick()
        }.firstOrNull().let {
            assertIs<CalculatorEffect.OpenBar>(it)
        }
    }

    @Test
    fun onSettingsClicked() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onSettingsClicked()
        }.firstOrNull().let {
            assertIs<CalculatorEffect.OpenSettings>(it)
        }
    }

    @Test
    fun onItemClick() = runTest {
        var currency = currency1
        viewModel.state.onSubscription {
            viewModel.event.onItemClick(currency1)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency1.code, it.base)
            assertEquals(currency1.rate, it.input)
        }

        // when last digit is . it should be removed
        currency = currency.copy(rate = "123.")

        viewModel.state.onSubscription {
            viewModel.event.onItemClick(currency)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency.code, it.base)
            assertEquals("123", it.input)
        }

        currency = currency.copy(rate = "123 456.78")

        viewModel.state.onSubscription {
            viewModel.event.onItemClick(currency)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency.code, it.base)
            assertEquals("123456.78", it.input)
        }
    }

    @Test
    fun onItemImageLongClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onItemImageLongClick(currency1)
        }.firstOrNull().let {
            assertIs<CalculatorEffect.ShowConversion>(it)
            assertEquals(
                currency1.getConversionStringFromBase(
                    viewModel.state.value.base,
                    viewModel.data.conversion
                ),
                it.text
            )
            assertEquals(currency1.code, it.code)

            verify(analyticsManager)
                .invocation { trackEvent(Event.ShowConversion(Param.Base(currency1.code))) }
                .wasInvoked()
        }
    }

    @Test
    fun onItemAmountLongClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onItemAmountLongClick(currency1.rate)
        }.firstOrNull().let {
            assertEquals(
                CalculatorEffect.CopyToClipboard(currency1.rate),
                it
            )

            verify(analyticsManager)
                .invocation { trackEvent(Event.CopyClipboard) }
                .wasInvoked()
        }
    }

    @Test
    fun onOutputLongClick() = runTest {
        val output = "5"
        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress(output)
            viewModel.event.onOutputLongClick()
        }.firstOrNull().let {
            assertEquals(CalculatorEffect.CopyToClipboard(output), it)

            verify(analyticsManager)
                .invocation { trackEvent(Event.CopyClipboard) }
                .wasInvoked()
        }
    }

    @Test
    fun onInputLongClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onInputLongClick()
        }.firstOrNull().let {
            assertEquals(CalculatorEffect.ShowPasteRequest, it)

            verify(analyticsManager)
                .invocation { trackEvent(Event.CopyClipboard) }
                .wasInvoked()
        }
    }

    @Test
    fun pasteToInput() = runTest {
        val text = "mock"
        val text2 = "mock 2"

        viewModel.state.onSubscription {
            viewModel.event.pasteToInput(text)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(text, it.input)

            verify(analyticsManager)
                .invocation { trackEvent(Event.PasteFromClipboard) }
                .wasInvoked()
        }

        viewModel.state.onSubscription {
            viewModel.event.pasteToInput(text2)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(text2.toSupportedCharacters(), it.input)

            verify(analyticsManager)
                .invocation { trackEvent(Event.PasteFromClipboard) }
                .wasInvoked()
        }
    }

    @Test
    fun onKeyPress() = runTest {
        val key = "1"

        // emits when input is empty
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress(key)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(key, it.input)
        }

        // adds when input is not empty
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress(key)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(key + key, it.input)
        }

        // resets to "" when AC clicked
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress(KEY_AC)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals("", it.input)
        }

        // does nothing when press DEL if input is already empty
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress(KEY_DEL)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals("", it.input)
        }

        // Deletes last digit when press DEL
        viewModel.state.onSubscription {
            viewModel.event.onKeyPress(key)
            viewModel.event.onKeyPress(key)
            viewModel.event.onKeyPress(KEY_DEL)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(key, it.input)
        }
    }

    @Test
    fun onBaseChanged() = runTest {
        given(calculationStorage)
            .invocation { currentBase }
            .thenReturn(currency1.code)

        given(backendApiService)
            .coroutine { getConversion(currency1.code) }
            .thenReturn(conversion)

        viewModel.state.onSubscription {
            viewModel.event.onBaseChange(currency1.code)
        }.firstOrNull().let {
            assertNotNull(it)
            assertNotNull(viewModel.data.conversion)
            assertEquals(currency1.code, viewModel.data.conversion!!.base)
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

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
import com.oztechan.ccc.client.core.shared.util.nowAsDateString
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
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.every
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
    private val calculationStorage =
        configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val conversionDataSource =
        configure(mock(classOf<ConversionDataSource>())) { stubsUnitByDefault = true }

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager =
        configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

    private val currency1 = Currency("USD", "Dollar", "$", "12345.678", true)
    private val currency2 = Currency("EUR", "Dollar", "$", "12345.678", true)
    private val currencyList = listOf(currency1, currency2)
    private val conversion = Conversion(currency1.code, "12.12.2121")
    private val shouldShowAds = Random.nextBoolean()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { calculationStorage.currentBase }
            .returns(currency1.code)

        every { calculationStorage.lastInput }
            .returns("")

        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(currencyList))

        every { calculationStorage.precision }
            .returns(3)

        every { adControlRepository.shouldShowBannerAd() }
            .returns(shouldShowAds)

        runTest {
            coEvery { currencyDataSource.getActiveCurrencies() }
                .returns(currencyList)

            coEvery { conversionDataSource.getConversionByBase(currency1.code) }
                .returns(conversion)

            coEvery { backendApiService.getConversion(currency1.code) }
                .returns(conversion)

            coEvery { currencyDataSource.getCurrencyByCode(currency1.code) }
                .returns(currency1)
        }
    }

    @Test
    fun `conversion should be fetched on init`() = runTest {
        viewModel
        coVerify { backendApiService.getConversion(currency1.code) }
            .wasInvoked()
        assertNotNull(viewModel.data.conversion)
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency1.code, it.base)
            assertEquals("", it.input)
            assertEquals("", it.output)
            assertEquals(currency1.symbol, it.symbol)
            assertIs<ConversionState.Online>(it.conversionState)
            assertEquals(ConversionState.Online(nowAsDateString()), it.conversionState)
            assertEquals(currencyList, it.currencyList)
            assertEquals(shouldShowAds, it.isBannerAdVisible)
            assertTrue { it.loading }
        }

        verify { adControlRepository.shouldShowBannerAd() }
            .wasInvoked()
    }

    @Test
    fun `init updates the latest base and input`() = runTest {
        val mock = "mock"

        every { calculationStorage.currentBase }
            .returns(currency1.code)

        every { calculationStorage.lastInput }
            .returns(mock)

        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(currency1.code, it.base)
            assertEquals(mock, it.input)
        }
    }

    @Test
    fun `init updates data correctly`() {
        assertNotNull(viewModel.data)
        assertNotNull(viewModel.data.conversion)
        assertNotNull(viewModel.data.parser)
    }

    @Test
    fun `when api fails and there is conversion in db then conversion rates are calculated`() =
        runTest {
            coEvery { backendApiService.getConversion(currency1.code) }
                .throws(Exception())

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

            coVerify { conversionDataSource.getConversionByBase(currency1.code) }
                .wasInvoked()
        }

    @Test
    fun `when api fails and there is no conversion in db then error state displayed`() = runTest {
        coEvery { backendApiService.getConversion(currency1.code) }
            .throws(Exception())

        coEvery { conversionDataSource.getConversionByBase(currency1.code) }
            .returns(null)

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

        coVerify { conversionDataSource.getConversionByBase(currency1.code) }
            .wasInvoked()
    }

    @Test
    fun `when there is few currency app doesn't make API call or search in DB`() = runTest {
        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(listOf(currency1)))

        viewModel.effect.onSubscription {
            viewModel.event.onKeyPress("1") // trigger api call
        }.firstOrNull().let {
            assertIs<CalculatorEffect.FewCurrency>(it)

            viewModel.state.value.let { state ->
                assertNotNull(state)
                assertFalse { state.loading }
                assertNull(viewModel.data.conversion)
                assertEquals(ConversionState.None, state.conversionState)
            }
        }

        coVerify { conversionDataSource.getConversionByBase(currency1.code) }
            .wasNotInvoked()

        coVerify { backendApiService.getConversion(currency1.code) }
            .wasNotInvoked()
    }

    @Test
    fun `when input is too long it should drop the last digit and give TooBigInput effect`() =
        runTest {
            val fortyFiveDigitNumber = "1234567890+1234567890+1234567890+1234567890+1"
            viewModel.effect.onSubscription {
                viewModel.event.onKeyPress(fortyFiveDigitNumber)
            }.firstOrNull().let {
                assertIs<CalculatorEffect.TooBigInput>(it)
                assertFalse { viewModel.state.value.loading }
                assertEquals(fortyFiveDigitNumber.dropLast(1), viewModel.state.value.input)
            }
        }

    @Test
    fun `when output is too long it should drop the last digit and give TooBigOutput effect`() =
        runTest {
            val nineteenDigitNumber = "123 567 901 345 789"
            viewModel.effect.onSubscription {
                viewModel.event.onKeyPress(nineteenDigitNumber)
            }.firstOrNull().let {
                assertIs<CalculatorEffect.TooBigOutput>(it)
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

        verify {
            analyticsManager.setUserProperty(
                UserProperty.CurrencyCount(currencyList.count().toString())
            )
        }.wasInvoked()
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

            verify { analyticsManager.trackEvent(Event.ShowConversion(Param.Base(currency1.code))) }
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

            verify { analyticsManager.trackEvent(Event.CopyClipboard) }
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

            verify { analyticsManager.trackEvent(Event.CopyClipboard) }
                .wasInvoked()
        }
    }

    @Test
    fun onInputLongClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onInputLongClick()
        }.firstOrNull().let {
            assertEquals(CalculatorEffect.ShowPasteRequest, it)
        }
    }

    @Test
    fun onPasteToInput() = runTest {
        val text = "mock"
        val text2 = "mock 2"

        viewModel.state.onSubscription {
            viewModel.event.onPasteToInput(text)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(text, it.input)

            verify { analyticsManager.trackEvent(Event.PasteFromClipboard) }
                .wasInvoked()
        }

        viewModel.state.onSubscription {
            viewModel.event.onPasteToInput(text2)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(text2.toSupportedCharacters(), it.input)

            verify { analyticsManager.trackEvent(Event.PasteFromClipboard) }
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
        every { calculationStorage.currentBase }
            .returns(currency1.code)

        coEvery { backendApiService.getConversion(currency1.code) }
            .returns(conversion)

        viewModel.state.onSubscription {
            viewModel.event.onBaseChange(currency1.code)
        }.firstOrNull().let {
            assertNotNull(it)
            assertNotNull(viewModel.data.conversion)
            assertEquals(currency1.code, viewModel.data.conversion!!.base)
            assertEquals(currency1.code, it.base)

            verify { analyticsManager.trackEvent(Event.BaseChange(Param.Base(currency1.code))) }
                .wasInvoked()

            verify { analyticsManager.setUserProperty(UserProperty.BaseCurrency(currency1.code)) }
                .wasInvoked()
        }
    }
}

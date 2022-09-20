/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.analytics.model.Param
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.RateState
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.calculateResult
import com.oztechan.ccc.client.util.getCurrencyConversionByRate
import com.oztechan.ccc.client.util.getFormatted
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import com.oztechan.ccc.client.model.Currency as CurrencyUIModel

@Suppress("OPT_IN_USAGE", "TooManyFunctions")
internal class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override val subject: CalculatorViewModel by lazy {
        CalculatorViewModel(
            settingsDataSource,
            backendApiService,
            currencyDataSource,
            offlineRatesDataSource,
            adRepository,
            analyticsManager
        )
    }

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val offlineRatesDataSource = mock(classOf<OfflineRatesDataSource>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val currency1 = Currency("USD", "Dollar", "$", 12345.678, true)
    private val currency2 = Currency("EUR", "Dollar", "$", 12345.678, true)
    private val currencyList = listOf(currency1, currency2)
    private val currencyUIModel = currency1.toUIModel()
    private val currencyResponse = CurrencyResponse(currency1.name, null, Rates())

    @BeforeTest
    override fun setup() {
        super.setup()

        given(settingsDataSource)
            .invocation { currentBase }
            .thenReturn(currency1.name)

        given(currencyDataSource)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyList))

        given(settingsDataSource)
            .invocation { precision }
            .thenReturn(3)

        runTest {
            given(offlineRatesDataSource)
                .coroutine { getOfflineRatesByBase(currency1.name) }
                .thenReturn(currencyResponse.rates)

            given(backendApiService)
                .coroutine { getRates(currency1.name) }
                .thenReturn(currencyResponse)

            given(currencyDataSource)
                .coroutine { getCurrencyByName(currency1.name) }
                .thenReturn(currency1)
        }
    }

    @Test
    fun when_api_fails_and_there_is_offline_rate_conversion_is_calculated() = runTest {
        given(backendApiService)
            .coroutine { getRates(currency1.name) }
            .thenThrow(Exception())

        subject.state.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertNotNull(it)
            assertFalse { it.loading }
            assertEquals(RateState.Offline(currencyResponse.rates.date), it.rateState)

            val result = currencyList.toUIModelList().onEach { currency ->
                currency.rate = currencyResponse.rates.calculateResult(currency.name, it.output)
                    .getFormatted(settingsDataSource.precision)
                    .toStandardDigits()
            }

            assertEquals(result, it.currencyList)
        }

        verify(offlineRatesDataSource)
            .coroutine { getOfflineRatesByBase(currency1.name) }
            .wasInvoked()
    }

    @Test
    fun when_api_fails_and_there_is_no_offline_rate_error_state_displayed() = runTest {
        given(backendApiService)
            .coroutine { getRates(currency1.name) }
            .thenThrow(Exception())

        given(offlineRatesDataSource)
            .coroutine { getOfflineRatesByBase(currency1.name) }
            .thenReturn(null)

        subject.effect.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertIs<CalculatorEffect.Error>(it)

            subject.state.value.let { state ->
                assertNotNull(state)
                assertFalse { state.loading }
                assertEquals(RateState.Error, state.rateState)
            }
        }

        verify(offlineRatesDataSource)
            .coroutine { getOfflineRatesByBase(currency1.name) }
            .wasInvoked()
    }

    @Test
    fun when_api_fails_and_there_is_no_offline_and_no_enough_currency_few_currency_effect_emitted() = runTest {
        given(backendApiService)
            .coroutine { getRates(currency1.name) }
            .thenThrow(Exception())

        given(offlineRatesDataSource)
            .coroutine { getOfflineRatesByBase(currency1.name) }
            .thenReturn(null)

        given(currencyDataSource)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(listOf(currency1)))

        subject.effect.before {
            subject.event.onKeyPress("1") // trigger api call
        }.after {
            assertIs<CalculatorEffect.FewCurrency>(it)

            subject.state.value.let { state ->
                assertNotNull(state)
                assertFalse { state.loading }
                assertEquals(RateState.Error, state.rateState)
            }
        }

        verify(offlineRatesDataSource)
            .coroutine { getOfflineRatesByBase(currency1.name) }
            .wasInvoked()
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        subject // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.CurrencyCount(currencyList.count().toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation {
                setUserProperty(
                    UserProperty.ActiveCurrencies(currencyList.joinToString(",") { currency -> currency.name })
                )
            }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, subject.shouldShowBannerAd())

        verify(adRepository)
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

    @Test
    fun onItemClick() {
        subject.state.before {
            subject.event.onItemClick(currencyUIModel)
        }.after {
            assertNotNull(it)
            assertEquals(currencyUIModel.name, it.base)
            assertEquals(currencyUIModel.rate, it.input)
        }

        // when last digit is . it should be removed
        val currency = CurrencyUIModel("USD", "", "", "123.")

        subject.state.before {
            subject.event.onItemClick(currency)
        }.after {
            assertNotNull(it)
            assertEquals(currency.name, it.base)
            assertEquals("123", it.input)
        }
    }

    @Test
    fun onItemImageLongClick() = subject.effect.before {
        subject.event.onItemImageLongClick(currencyUIModel)
    }.after {
        assertIs<CalculatorEffect.ShowRate>(it)
        assertEquals(
            currencyUIModel.getCurrencyConversionByRate(
                subject.state.value.base,
                subject.data.rates
            ),
            it.text
        )
        assertEquals(currencyUIModel.name, it.name)

        verify(analyticsManager)
            .invocation { trackEvent(Event.ShowConversion(Param.Base(currencyUIModel.name))) }
            .wasInvoked()
    }

    @Test
    fun onItemAmountLongClick() = subject.effect.before {
        subject.event.onItemAmountLongClick(currencyUIModel.rate)
    }.after {
        assertEquals(
            CalculatorEffect.CopyToClipboard(currencyUIModel.rate),
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
        given(settingsDataSource)
            .invocation { currentBase }
            .thenReturn(currency1.name)

        runTest {
            given(backendApiService)
                .coroutine { getRates(currency1.name) }
                .thenReturn(currencyResponse)
        }

        subject.state.before {
            subject.event.onBaseChange(currency1.name)
        }.after {
            assertNotNull(it)
            assertNotNull(subject.data.rates)
            assertEquals(currency1.name, subject.data.rates!!.base)
            assertEquals(currency1.name, it.base)

            verify(analyticsManager)
                .invocation { trackEvent(Event.BaseChange(Param.Base(currency1.name))) }
                .wasInvoked()

            verify(analyticsManager)
                .invocation { setUserProperty(UserProperty.BaseCurrency(currency1.name)) }
                .wasInvoked()
        }
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.analytics.model.Param
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.util.getCurrencyConversionByRate
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
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class CalculatorViewModelTest : BaseViewModelTest() {

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

    private val viewModel: CalculatorViewModel by lazy {
        CalculatorViewModel(
            settingsDataSource,
            backendApiService,
            currencyDataSource,
            offlineRatesDataSource,
            adRepository,
            analyticsManager
        )
    }

    private val currency = Currency("USD", "Dollar", "$", 12345.678, true)
    private val currencyUIModel = currency.toUIModel()
    private val currencyResponse = CurrencyResponse(currency.name, null, Rates())

    @BeforeTest
    fun setup() {
        given(settingsDataSource)
            .invocation { currentBase }
            .thenReturn(currency.name)

        given(currencyDataSource)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flow { listOf(currency) })

        runTest {
            given(offlineRatesDataSource)
                .coroutine { getOfflineRatesByBase(currency.name) }
                .thenReturn(currencyResponse.rates)

            given(backendApiService)
                .coroutine { getRates(currency.name) }
                .thenReturn(currencyResponse)

            given(currencyDataSource)
                .coroutine { getCurrencyByName(currency.name) }
                .thenReturn(currency)
        }
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, viewModel.shouldShowBannerAd())

        verify(adRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    // Event

    @Test
    fun onBarClick() = viewModel.effect.before {
        viewModel.event.onBarClick()
    }.after {
        assertIs<CalculatorEffect.OpenBar>(it)
    }

    @Test
    fun onSettingsClicked() = viewModel.effect.before {
        viewModel.event.onSettingsClicked()
    }.after {
        assertIs<CalculatorEffect.OpenSettings>(it)
    }

    @Test
    fun onItemClick() = viewModel.state.before {
        viewModel.event.onItemClick(currencyUIModel)
    }.after {
        assertNotNull(it)
        assertEquals(currencyUIModel.name, it.base)
        assertEquals(currencyUIModel.rate.toString(), it.input)
    }

    @Test
    fun onItemImageLongClick() = viewModel.effect.before {
        viewModel.event.onItemImageLongClick(currencyUIModel)
    }.after {
        assertIs<CalculatorEffect.ShowRate>(it)
        assertEquals(
            currencyUIModel.getCurrencyConversionByRate(
                viewModel.state.value.base,
                viewModel.data.rates
            ),
            it.text
        )
        assertEquals(currencyUIModel.name, it.name)

        verify(analyticsManager)
            .invocation { trackEvent(Event.ShowConversion(Param.Base(currencyUIModel.name))) }
            .wasInvoked()
    }

    @Test
    fun onItemAmountLongClick() = viewModel.effect.before {
        viewModel.event.onItemAmountLongClick(currencyUIModel.rate.toString())
    }.after {
        assertEquals(
            CalculatorEffect.CopyToClipboard(currencyUIModel.rate.toString()),
            it
        )

        verify(analyticsManager)
            .invocation { trackEvent(Event.CopyClipboard) }
            .wasInvoked()
    }

    @Test
    fun onKeyPress() = with(viewModel) {
        val key = "1"

        state.before {
            event.onKeyPress(key)
        }.after {
            assertNotNull(it)
            assertEquals(key, it.input)
        }

        state.before {
            event.onKeyPress(KEY_AC)
        }.after {
            assertNotNull(it)
            assertEquals("", it.input)
        }

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
            .thenReturn(currency.name)

        runTest {
            given(backendApiService)
                .coroutine { getRates(currency.name) }
                .thenReturn(currencyResponse)
        }

        viewModel.state.before {
            viewModel.event.onBaseChange(currency.name)
        }.after {
            assertNotNull(it)
            assertEquals(currency.name, viewModel.data.rates?.base)
            assertNotNull(viewModel.data.rates)
            assertEquals(currency.name, it.base)

            verify(analyticsManager)
                .invocation { trackEvent(Event.BaseChange(Param.Base(currency.name))) }
                .wasInvoked()
        }
    }
}

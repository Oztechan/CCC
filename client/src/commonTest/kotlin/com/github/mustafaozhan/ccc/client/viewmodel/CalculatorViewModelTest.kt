/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.mapper.toUIModel
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getRandomDateLong
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.model.AdConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CalculatorViewModelTest {

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    @Mock
    private val apiRepository = mock(classOf<ApiRepository>())

    @Mock
    private val currencyRepository = mock(classOf<CurrencyRepository>())

    @Mock
    private val offlineRatesRepository = mock(classOf<OfflineRatesRepository>())

    @Mock
    private val configManager = mock(classOf<ConfigManager>())

    private val viewModel: CalculatorViewModel by lazy {
        CalculatorViewModel(
            settingsRepository,
            apiRepository,
            currencyRepository,
            offlineRatesRepository,
            configManager
        )
    }

    private val currency = Currency("USD", "Dollar", "$", 12345.6789, true)
    private val currencyUIModel = currency.toUIModel()
    private val currencyResponse = CurrencyResponse(currency.name, null, Rates())

    @ConfigurationApi
    @BeforeTest
    fun setup() {
        initLogger(true)

        given(settingsRepository)
            .invocation { currentBase }
            .thenReturn("")

        given(settingsRepository)
            .setter(settingsRepository::currentBase)
            .whenInvokedWith(any())
            .thenReturn(Unit)

        given(currencyRepository)
            .function(currencyRepository::getCurrencyByName)
            .whenInvokedWith(any())
            .thenReturn(currency)

        runTest {
            given(apiRepository)
                .suspendFunction(apiRepository::getRatesByBackend)
                .whenInvokedWith(any())
                .thenReturn(currencyResponse)
        }

        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flow { listOf(currency) })

        given(offlineRatesRepository)
            .function(offlineRatesRepository::getOfflineRatesByBase)
            .whenInvokedWith(any())
            .thenReturn(null)

        given(offlineRatesRepository)
            .function(offlineRatesRepository::insertOfflineRates)
            .whenInvokedWith(any())
            .thenReturn(Unit)
    }

    @Test
    fun shouldShowBannerAd() {
        val mockLong = Random.getRandomDateLong()
        val mockBoolean = Random.nextBoolean()
        val mockBannerSessionCount = Random.nextInt()
        val mockSessionCount = Random.nextLong()

        val mockAppConfig = AppConfig(AdConfig(bannerAdSessionCount = mockBannerSessionCount))

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockSessionCount)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(mockBoolean)

        assertEquals(
            !mockBoolean && mockLong.isRewardExpired() && mockSessionCount > mockBannerSessionCount,
            viewModel.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    // Event

    @Test
    fun onBarClick() = viewModel.effect.before {
        viewModel.event.onBarClick()
    }.after {
        assertEquals(CalculatorEffect.OpenBar, it)
    }

    @Test
    fun onSettingsClicked() = viewModel.effect.before {
        viewModel.event.onSettingsClicked()
    }.after {
        assertEquals(CalculatorEffect.OpenSettings, it)
    }

    @Test
    fun onItemClick() = viewModel.state.before {
        viewModel.event.onItemClick(currencyUIModel)
    }.after {
        assertEquals(currencyUIModel.name, it?.base)
        assertEquals(currencyUIModel.rate.toString(), it?.input)
    }

    @Test
    fun onItemLongClick() = viewModel.effect.before {
        viewModel.event.onItemLongClick(currencyUIModel)
    }.after {
        assertEquals(
            CalculatorEffect.ShowRate(
                currencyUIModel.getCurrencyConversionByRate(
                    viewModel.state.value.base,
                    viewModel.data.rates
                ),
                currencyUIModel.name
            ),
            it
        )
    }

    @Test
    fun onKeyPress() = with(viewModel) {
        val key = "1"

        state.before {
            event.onKeyPress(key)
        }.after {
            assertEquals(key, it?.input)
        }

        state.before {
            event.onKeyPress(KEY_AC)
        }.after {
            assertEquals("", it?.input)
        }

        state.before {
            event.onKeyPress(key)
            event.onKeyPress(key)
            event.onKeyPress(KEY_DEL)
        }.after {
            assertEquals(key, it?.input)
        }
    }

    @Test
    fun onBaseChanged() = viewModel.state.before {
        viewModel.event.onBaseChange(currency.name)
    }.after {
        assertEquals(currency.name, viewModel.data.rates?.base)
        assertNotNull(viewModel.data.rates)
        assertEquals(currency.name, it?.base)
    }
}

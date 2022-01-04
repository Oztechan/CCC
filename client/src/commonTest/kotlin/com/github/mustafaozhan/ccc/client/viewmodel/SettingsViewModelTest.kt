/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsState
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.settings.update
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.RemoteConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ConfigurationApi
@Suppress("TooManyFunctions")
class SettingsViewModelTest {

    @Mock
    private val settingsRepository = configure(mock(classOf<SettingsRepository>())) {
        stubsUnitByDefault = true
    }

    @Mock
    private val apiRepository = mock(classOf<ApiRepository>())

    @Mock
    private val currencyRepository = mock(classOf<CurrencyRepository>())

    @Mock
    private val offlineRatesRepository = mock(classOf<OfflineRatesRepository>())

    @Mock
    private val remoteConfig = mock(classOf<RemoteConfig>())

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(
            settingsRepository,
            apiRepository,
            currencyRepository,
            offlineRatesRepository,
            remoteConfig
        )
    }

    private val currencyList = listOf(
        Currency("", "", ""),
        Currency("", "", "")
    )

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(settingsRepository)
            .getter(settingsRepository::appTheme)
            .whenInvoked()
            .thenReturn(-1)

        given(settingsRepository)
            .getter(settingsRepository::adFreeEndDate)
            .whenInvoked()
            .thenReturn(0)

        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyList))
    }

    // SEED
    @Test
    fun states_updates_correctly() {

        val state = MutableStateFlow(SettingsState())

        val activeCurrencyCount = Random.nextInt()
        val appThemeType = AppTheme.getThemeByOrder(Random.nextInt() % 3) ?: AppTheme.SYSTEM_DEFAULT
        val addFreeEndDate = "23.12.2121"
        val loading = Random.nextBoolean()

        state.before {
            state.update(
                activeCurrencyCount = activeCurrencyCount,
                appThemeType = appThemeType,
                addFreeEndDate = addFreeEndDate,
                loading = loading
            )
        }.after {
            assertEquals(activeCurrencyCount, it?.activeCurrencyCount)
            assertEquals(appThemeType, it?.appThemeType)
            assertEquals(addFreeEndDate, it?.addFreeEndDate)
            assertEquals(loading, it?.loading)
        }
    }

    // init
    @Test
    fun init_updates_states_correctly() = runTest {
        viewModel.state.firstOrNull().let {
            assertEquals(AppTheme.SYSTEM_DEFAULT, it?.appThemeType) // mocked -1
            assertEquals(currencyList.size, it?.activeCurrencyCount)
        }
    }

    // public methods
    @Test
    fun updateTheme() {
        val mockTheme = AppTheme.DARK

        with(viewModel) {
            effect.before {
                updateTheme(mockTheme)
            }.after {
                assertEquals(mockTheme, state.value.appThemeType)
                assertEquals(SettingsEffect.ChangeTheme(mockTheme.themeValue), it)
            }
        }

        verify(settingsRepository)
            .invocation { appTheme = mockTheme.themeValue }
            .wasInvoked()
    }

    @Test
    fun isRewardExpired() {
        assertEquals(
            settingsRepository.adFreeEndDate.isRewardExpired(),
            viewModel.isRewardExpired()
        )
        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd() {
        val mockValue = Random.nextLong()
        val mockAppConfig = AppConfig()
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockValue)

        given(remoteConfig)
            .getter(remoteConfig::appConfig)
            .whenInvoked()
            .thenReturn(mockAppConfig)

        assertEquals(
            viewModel.isRewardExpired() && mockAppConfig.adConfig.isBannerAdEnabled,
            viewModel.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(remoteConfig)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated() {
        assertEquals(
            true, // mock is returning 0
            viewModel.isAdFreeNeverActivated()
        )
        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun updateAddFreeDate() {
        viewModel.state.before {
            viewModel.updateAddFreeDate()
        }.after {
            assertEquals(true, it?.addFreeEndDate?.isNotEmpty())

            verify(settingsRepository)
                .invocation { adFreeEndDate = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun getAppTheme() {
        viewModel.state.before {
            viewModel.updateAddFreeDate()
        }.after {
            assertEquals(true, it?.addFreeEndDate?.isNotEmpty())

            verify(settingsRepository)
                .invocation { adFreeEndDate = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()) }
        }
    }

    // Event
    @Test
    fun onBackClick() = viewModel.effect.before {
        viewModel.event.onBackClick()
    }.after {
        assertTrue { it is SettingsEffect.Back }
    }

    @Test
    fun onCurrenciesClick() = viewModel.effect.before {
        viewModel.event.onCurrenciesClick()
    }.after {
        assertTrue { it is SettingsEffect.OpenCurrencies }
    }

    @Test
    fun onFeedBackClick() = viewModel.effect.before {
        viewModel.event.onFeedBackClick()
    }.after {
        assertTrue { it is SettingsEffect.FeedBack }
    }

    @Test
    fun onShareClick() = viewModel.effect.before {
        viewModel.event.onShareClick()
    }.after {
        assertTrue { it is SettingsEffect.Share }
    }

    @Test
    fun onSupportUsClick() = viewModel.effect.before {
        viewModel.event.onSupportUsClick()
    }.after {
        assertTrue { it is SettingsEffect.SupportUs }
    }

    @Test
    fun onOnGitHubClick() = viewModel.effect.before {
        viewModel.event.onOnGitHubClick()
    }.after {
        assertTrue { it is SettingsEffect.OnGitHub }
    }

    @Test
    fun onRemoveAdsClick() {
        viewModel.effect.before {
            viewModel.event.onRemoveAdsClick()
        }.after {
            assertTrue { it is SettingsEffect.RemoveAds }
        }

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun onThemeClick() = viewModel.effect.before {
        viewModel.event.onThemeClick()
    }.after {
        assertTrue { it is SettingsEffect.ThemeDialog }
    }

    @Test
    fun onSyncClick() {
        given(currencyRepository)
            .function(currencyRepository::getActiveCurrencies)
            .whenInvoked()
            .thenReturn(listOf())

        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.state.value.loading }
            assertTrue { it is SettingsEffect.Synchronising }
        }

        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.data.synced }
            assertTrue { it is SettingsEffect.OnlyOneTimeSync }
        }
    }
}

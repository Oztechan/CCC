/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.manager.session.SessionManager
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.viewmodel.settings.SettingsEffect
import com.oztechan.ccc.client.viewmodel.settings.SettingsState
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.settings.update
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.nowAsLong
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest() {

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyRepository = mock(classOf<CurrencyRepository>())

    @Mock
    private val offlineRatesRepository = mock(classOf<OfflineRatesRepository>())

    @Mock
    private val watcherRepository = mock(classOf<WatcherRepository>())

    @Mock
    private val sessionManager = mock(classOf<SessionManager>())

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(
            settingsRepository,
            backendApiService,
            currencyRepository,
            offlineRatesRepository,
            watcherRepository,
            sessionManager
        )
    }

    private val currencyList = listOf(
        Currency("", "", ""),
        Currency("", "", "")
    )

    private val watcherLists = listOf(
        Watcher(1, "EUR", "USD", true, 1.1),
        Watcher(2, "USD", "EUR", false, 2.3)
    )

    @BeforeTest
    fun setup() {
        given(settingsRepository)
            .invocation { appTheme }
            .thenReturn(-1)

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyList))

        given(watcherRepository)
            .invocation { collectWatchers() }
            .then { flowOf(watcherLists) }
    }

    // SEED
    @Test
    fun states_updates_correctly() {

        val state = MutableStateFlow(SettingsState())

        val activeCurrencyCount = Random.nextInt()
        val activeWatcherCount = Random.nextInt()
        val appThemeType = AppTheme.getThemeByOrderOrDefault(Random.nextInt() % 3)
        val addFreeEndDate = "23.12.2121"
        val loading = Random.nextBoolean()

        state.before {
            state.update(
                activeCurrencyCount = activeCurrencyCount,
                activeWatcherCount = activeWatcherCount,
                appThemeType = appThemeType,
                addFreeEndDate = addFreeEndDate,
                loading = loading
            )
        }.after {
            assertEquals(activeCurrencyCount, it?.activeCurrencyCount)
            assertEquals(activeWatcherCount, it?.activeWatcherCount)
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
            assertEquals(watcherLists.size, it?.activeWatcherCount)
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

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + DAY)

        viewModel.effect.before {
            viewModel.event.onRemoveAdsClick()
        }.after {
            assertTrue { it is SettingsEffect.AlreadyAdFree }
        }

        verify(settingsRepository)
            .invocation { adFreeEndDate }
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
        val mockBoolean = Random.nextBoolean()

        given(sessionManager)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, viewModel.shouldShowBannerAd())

        verify(sessionManager)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_false_when_adFreeEndDate_is_not_zero() {
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(1)

        assertFalse { viewModel.isAdFreeNeverActivated() }

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_true_when_adFreeEndDate_is_zero() {
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        assertTrue { viewModel.isAdFreeNeverActivated() }

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
    fun onWatchersClicked() = viewModel.effect.before {
        viewModel.event.onWatchersClicked()
    }.after {
        assertEquals(SettingsEffect.OpenWatchers, it)
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

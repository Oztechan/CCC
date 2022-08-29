/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.indexToNumber
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.viewmodel.settings.SettingsEffect
import com.oztechan.ccc.client.viewmodel.settings.SettingsState
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.settings.update
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.nowAsLong
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.eq
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
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class SettingsViewModelTest : BaseViewModelTest() {

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val offlineRatesDataSource = mock(classOf<OfflineRatesDataSource>())

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(
            settingsDataSource,
            backendApiService,
            currencyDataSource,
            offlineRatesDataSource,
            watcherDataSource,
            adRepository,
            appConfigRepository,
            analyticsManager
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

    private val mockedPrecision = 3

    @BeforeTest
    fun setup() {
        given(settingsDataSource)
            .invocation { appTheme }
            .thenReturn(-1)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        given(settingsDataSource)
            .invocation { precision }
            .thenReturn(mockedPrecision)

        given(currencyDataSource)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyList))

        given(watcherDataSource)
            .invocation { collectWatchers() }
            .then { flowOf(watcherLists) }

        given(appConfigRepository)
            .invocation { getDeviceType() }
            .then { Device.IOS }
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
        val precision = Random.nextInt()

        state.before {
            state.update(
                activeCurrencyCount = activeCurrencyCount,
                activeWatcherCount = activeWatcherCount,
                appThemeType = appThemeType,
                addFreeEndDate = addFreeEndDate,
                loading = loading,
                precision = precision
            )
        }.after {
            assertNotNull(it)
            assertEquals(activeCurrencyCount, it.activeCurrencyCount)
            assertEquals(activeWatcherCount, it.activeWatcherCount)
            assertEquals(appThemeType, it.appThemeType)
            assertEquals(addFreeEndDate, it.addFreeEndDate)
            assertEquals(loading, it.loading)
            assertEquals(precision, it.precision)
        }
    }

    // init
    @Test
    fun init_updates_states_correctly() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(AppTheme.SYSTEM_DEFAULT, it.appThemeType) // mocked -1
            assertEquals(currencyList.size, it.activeCurrencyCount)
            assertEquals(watcherLists.size, it.activeWatcherCount)
            assertEquals(mockedPrecision, it.precision)
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
                assertIs<SettingsEffect.ChangeTheme>(it)
                assertEquals(mockTheme.themeValue, it.themeValue)
            }
        }

        verify(settingsDataSource)
            .invocation { appTheme = mockTheme.themeValue }
            .wasInvoked()

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + DAY)

        viewModel.effect.before {
            viewModel.event.onRemoveAdsClick()
        }.after {
            assertIs<SettingsEffect.AlreadyAdFree>(it)
        }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isRewardExpired() {
        assertEquals(
            settingsDataSource.adFreeEndDate.isRewardExpired(),
            viewModel.isRewardExpired()
        )
        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
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

    @Test
    fun shouldShowRemoveAds() {
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowRemoveAds() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, viewModel.shouldShowRemoveAds())

        verify(adRepository)
            .invocation { shouldShowRemoveAds() }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_false_when_adFreeEndDate_is_not_zero() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(1)

        assertFalse { viewModel.isAdFreeNeverActivated() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_true_when_adFreeEndDate_is_zero() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        assertTrue { viewModel.isAdFreeNeverActivated() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun updateAddFreeDate() {
        viewModel.state.before {
            viewModel.updateAddFreeDate()
        }.after {
            assertNotNull(it)
            assertTrue { it.addFreeEndDate.isNotEmpty() }

            verify(settingsDataSource)
                .invocation { adFreeEndDate = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun getAppTheme() {
        viewModel.state.before {
            viewModel.updateAddFreeDate()
        }.after {
            assertNotNull(it)
            assertTrue { it.addFreeEndDate.isNotEmpty() }

            verify(settingsDataSource)
                .invocation { adFreeEndDate = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()) }
        }
    }

    // Event
    @Test
    fun onBackClick() = viewModel.effect.before {
        viewModel.event.onBackClick()
    }.after {
        assertIs<SettingsEffect.Back>(it)
    }

    @Test
    fun onCurrenciesClick() = viewModel.effect.before {
        viewModel.event.onCurrenciesClick()
    }.after {
        assertIs<SettingsEffect.OpenCurrencies>(it)
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
        assertIs<SettingsEffect.FeedBack>(it)
    }

    @Test
    fun onShareClick() {
        val link = "link"

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { link }

        viewModel.effect.before {
            viewModel.event.onShareClick()
        }.after {
            assertIs<SettingsEffect.Share>(it)
            assertEquals(link, it.marketLink)
        }
    }

    @Test
    fun onSupportUsClick() {
        val link = "link"

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { link }

        viewModel.effect.before {
            viewModel.event.onSupportUsClick()
        }.after {
            assertIs<SettingsEffect.SupportUs>(it)
            assertEquals(link, it.marketLink)
        }
    }

    @Test
    fun onOnGitHubClick() = viewModel.effect.before {
        viewModel.event.onOnGitHubClick()
    }.after {
        assertIs<SettingsEffect.OnGitHub>(it)
    }

    @Test
    fun onRemoveAdsClick() {
        viewModel.effect.before {
            viewModel.event.onRemoveAdsClick()
        }.after {
            assertIs<SettingsEffect.RemoveAds>(it)
        }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun onThemeClick() = viewModel.effect.before {
        viewModel.event.onThemeClick()
    }.after {
        assertIs<SettingsEffect.ThemeDialog>(it)
    }

    @Test
    fun onSyncClick() {
        runTest {
            given(currencyDataSource)
                .coroutine { currencyDataSource.getActiveCurrencies() }
                .thenReturn(listOf())
        }

        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.state.value.loading }
            assertIs<SettingsEffect.Synchronising>(it)
        }

        viewModel.effect.before {
            viewModel.event.onSyncClick()
        }.after {
            assertTrue { viewModel.data.synced }
            assertIs<SettingsEffect.OnlyOneTimeSync>(it)
        }

        verify(analyticsManager)
            .invocation { trackEvent(Event.OfflineSync) }
            .wasInvoked()
    }

    @Test
    fun onPrecisionClick() = viewModel.effect.before {
        viewModel.event.onPrecisionClick()
    }.after {
        assertIs<SettingsEffect.SelectPrecision>(it)
    }

    @Test
    fun onPrecisionSelect() {
        val value = Random.nextInt()
        viewModel.state.before {
            viewModel.event.onPrecisionSelect(value)
        }.after {
            assertNotNull(it)
            assertEquals(value.indexToNumber(), it.precision)

            println("-----")
            verify(settingsDataSource)
                .setter(settingsDataSource::precision)
                .with(eq(value.indexToNumber()))
                .wasInvoked()
        }
    }
}

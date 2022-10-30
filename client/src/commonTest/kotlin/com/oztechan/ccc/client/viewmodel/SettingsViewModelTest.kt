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
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.util.indexToNumber
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.client.viewmodel.settings.SettingsEffect
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.eq
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
import kotlin.test.assertTrue

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override val subject: SettingsViewModel by lazy {
        SettingsViewModel(
            appStorage,
            calculatorStorage,
            backendApiService,
            currencyDataSource,
            offlineRatesDataSource,
            watcherDataSource,
            adRepository,
            appConfigRepository,
            analyticsManager
        )
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @Mock
    private val calculatorStorage = mock(classOf<CalculatorStorage>())

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

    private val currencyList = listOf(
        Currency("", "", ""),
        Currency("", "", "")
    )

    private val watcherLists = listOf(
        Watcher(1, "EUR", "USD", true, 1.1),
        Watcher(2, "USD", "EUR", false, 2.3)
    )

    private val mockedPrecision = 3
    private val version = "version"

    @BeforeTest
    override fun setup() {
        super.setup()

        given(appStorage)
            .invocation { appTheme }
            .thenReturn(-1)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        given(calculatorStorage)
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

        given(appConfigRepository)
            .invocation { getVersion() }
            .then { version }
    }

    // init
    @Test
    fun init_updates_states_correctly() = runTest {
        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(AppTheme.SYSTEM_DEFAULT, it.appThemeType) // mocked -1
            assertEquals(currencyList.size, it.activeCurrencyCount)
            assertEquals(watcherLists.size, it.activeWatcherCount)
            assertEquals(mockedPrecision, it.precision)
            assertEquals(version, it.version)
        }
    }

    @Test
    fun `successful synchroniseRates update the database`() = runTest {
        subject.data.synced = false

        val currencyResponse = CurrencyResponse("EUR", null, Rates())
        val currency = Currency("EUR", "", "")

        given(currencyDataSource)
            .coroutine { currencyDataSource.getActiveCurrencies() }
            .thenReturn(currencyList)

        given(backendApiService)
            .coroutine { getRates(currency.name) }
            .thenReturn(currencyResponse)

        subject.effect.before {
            subject.event.onSyncClick()
        }.after {
            assertTrue { subject.state.value.loading }
            assertIs<SettingsEffect.Synchronising>(it)
        }

        verify(offlineRatesDataSource)
            .coroutine { offlineRatesDataSource.insertOfflineRates(currencyResponse) }
            .wasInvoked()
    }

    @Test
    fun `failed synchroniseRates should pass Synchronised effect`() = runTest {
        subject.data.synced = false

        given(currencyDataSource)
            .coroutine { currencyDataSource.getActiveCurrencies() }
            .thenReturn(currencyList)

        given(backendApiService)
            .coroutine { getRates("") }
            .thenThrow(Exception("test"))

        subject.effect.before {
            subject.event.onSyncClick()
        }.after {
            assertTrue { subject.state.value.loading }
            assertIs<SettingsEffect.Synchronising>(it)
        }

        verify(offlineRatesDataSource)
            .coroutine { offlineRatesDataSource.insertOfflineRates(CurrencyResponse("", "", Rates())) }
            .wasNotInvoked()
    }

    // public methods
    @Test
    fun updateTheme() {
        val mockTheme = AppTheme.DARK

        with(subject) {
            effect.before {
                updateTheme(mockTheme)
            }.after {
                assertEquals(mockTheme, state.value.appThemeType)
                assertIs<SettingsEffect.ChangeTheme>(it)
                assertEquals(mockTheme.themeValue, it.themeValue)
            }
        }

        verify(appStorage)
            .invocation { appTheme = mockTheme.themeValue }
            .wasInvoked()

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + DAY)

        subject.effect.before {
            subject.event.onRemoveAdsClick()
        }.after {
            assertIs<SettingsEffect.AlreadyAdFree>(it)
        }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isRewardExpired() {
        assertEquals(
            appStorage.adFreeEndDate.isRewardExpired(),
            subject.isRewardExpired()
        )
        verify(appStorage)
            .invocation { adFreeEndDate }
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

    @Test
    fun shouldShowRemoveAds() {
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowRemoveAds() }
            .thenReturn(mockBoolean)

        assertEquals(mockBoolean, subject.shouldShowRemoveAds())

        verify(adRepository)
            .invocation { shouldShowRemoveAds() }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_false_when_adFreeEndDate_is_not_zero() {
        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(1)

        assertFalse { subject.isAdFreeNeverActivated() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFreeNeverActivated_returns_true_when_adFreeEndDate_is_zero() {
        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        assertTrue { subject.isAdFreeNeverActivated() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun updateAddFreeDate() {
        subject.state.before {
            subject.updateAddFreeDate()
        }.after {
            assertNotNull(it)
            assertTrue { it.addFreeEndDate.isNotEmpty() }

            verify(appStorage)
                .invocation { adFreeEndDate = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun getAppTheme() {
        assertEquals(-1, subject.getAppTheme()) // already mocked
    }

    // Event
    @Test
    fun onBackClick() = subject.effect.before {
        subject.event.onBackClick()
    }.after {
        assertIs<SettingsEffect.Back>(it)
    }

    @Test
    fun onCurrenciesClick() = subject.effect.before {
        subject.event.onCurrenciesClick()
    }.after {
        assertIs<SettingsEffect.OpenCurrencies>(it)
    }

    @Test
    fun onWatchersClicked() = subject.effect.before {
        subject.event.onWatchersClicked()
    }.after {
        assertEquals(SettingsEffect.OpenWatchers, it)
    }

    @Test
    fun onFeedBackClick() = subject.effect.before {
        subject.event.onFeedBackClick()
    }.after {
        assertIs<SettingsEffect.FeedBack>(it)
    }

    @Test
    fun onShareClick() {
        val link = "link"

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { link }

        subject.effect.before {
            subject.event.onShareClick()
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

        subject.effect.before {
            subject.event.onSupportUsClick()
        }.after {
            assertIs<SettingsEffect.SupportUs>(it)
            assertEquals(link, it.marketLink)
        }
    }

    @Test
    fun onOnGitHubClick() = subject.effect.before {
        subject.event.onOnGitHubClick()
    }.after {
        assertIs<SettingsEffect.OnGitHub>(it)
    }

    @Test
    fun onRemoveAdsClick() {
        subject.effect.before {
            subject.event.onRemoveAdsClick()
        }.after {
            assertIs<SettingsEffect.RemoveAds>(it)
        }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun onThemeClick() = subject.effect.before {
        subject.event.onThemeClick()
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

        subject.effect.before {
            subject.event.onSyncClick()
        }.after {
            assertTrue { subject.state.value.loading }
            assertIs<SettingsEffect.Synchronising>(it)
        }

        subject.effect.before {
            subject.event.onSyncClick()
        }.after {
            assertTrue { subject.data.synced }
            assertIs<SettingsEffect.OnlyOneTimeSync>(it)
        }

        verify(analyticsManager)
            .invocation { trackEvent(Event.OfflineSync) }
            .wasInvoked()
    }

    @Test
    fun onPrecisionClick() = subject.effect.before {
        subject.event.onPrecisionClick()
    }.after {
        assertIs<SettingsEffect.SelectPrecision>(it)
    }

    @Test
    fun onPrecisionSelect() {
        val value = Random.nextInt()
        subject.state.before {
            subject.event.onPrecisionSelect(value)
        }.after {
            assertNotNull(it)
            assertEquals(value.indexToNumber(), it.precision)

            println("-----")
            verify(calculatorStorage)
                .setter(calculatorStorage::precision)
                .with(eq(value.indexToNumber()))
                .wasInvoked()
        }
    }
}

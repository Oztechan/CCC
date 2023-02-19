/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.settings

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.shared.model.PremiumType
import com.oztechan.ccc.client.core.shared.util.calculatePremiumEnd
import com.oztechan.ccc.client.core.shared.util.indexToNumber
import com.oztechan.ccc.client.core.shared.util.isPassed
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.Watcher
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.eq
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
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class SettingsViewModelTest {

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(
            appStorage,
            calculationStorage,
            backendApiService,
            currencyDataSource,
            conversionDataSource,
            watcherDataSource,
            adControlRepository,
            appConfigRepository,
            analyticsManager
        )
    }

    @Mock
    private val appStorage = configure(mock(classOf<AppStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val calculationStorage = configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val conversionDataSource = mock(classOf<ConversionDataSource>())

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager = configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

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
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        Dispatchers.setMain(UnconfinedTestDispatcher())

        given(appStorage)
            .invocation { appTheme }
            .thenReturn(-1)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        given(calculationStorage)
            .invocation { precision }
            .thenReturn(mockedPrecision)

        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(currencyList))

        given(watcherDataSource)
            .invocation { getWatchersFlow() }
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
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(AppTheme.SYSTEM_DEFAULT, it.appThemeType) // mocked -1
            assertEquals(currencyList.size, it.activeCurrencyCount)
            assertEquals(watcherLists.size, it.activeWatcherCount)
            assertEquals(mockedPrecision, it.precision)
            assertEquals(version, it.version)
        }
    }

    @Test
    fun `successful synchroniseConversions update the database`() = runTest {
        viewModel.data.synced = false

        val base = "EUR"
        val conversion = Conversion(base)
        val currency = Currency(base, "", "")

        given(currencyDataSource)
            .coroutine { currencyDataSource.getActiveCurrencies() }
            .thenReturn(currencyList)

        given(backendApiService)
            .coroutine { getConversion(currency.code) }
            .thenReturn(conversion)

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Synchronising>(it)
        }

        verify(conversionDataSource)
            .coroutine { conversionDataSource.insertConversion(conversion) }
            .wasInvoked()
    }

    @Test
    fun `failed synchroniseConversions should pass Synchronised effect`() = runTest {
        viewModel.data.synced = false

        given(currencyDataSource)
            .coroutine { currencyDataSource.getActiveCurrencies() }
            .thenReturn(currencyList)

        given(backendApiService)
            .coroutine { getConversion("") }
            .thenThrow(Exception("test"))

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Synchronising>(it)
        }

        verify(conversionDataSource)
            .coroutine { conversionDataSource.insertConversion(Conversion()) }
            .wasNotInvoked()
    }

    // public methods
    @Test
    fun updateTheme() = runTest {
        val mockTheme = AppTheme.DARK

        viewModel.effect.onSubscription {
            viewModel.updateTheme(mockTheme)
        }.firstOrNull().let {
            assertEquals(mockTheme, viewModel.state.value.appThemeType)
            assertIs<SettingsEffect.ChangeTheme>(it)
            assertEquals(mockTheme.themeValue, it.themeValue)
        }

        verify(appStorage)
            .invocation { appTheme = mockTheme.themeValue }
            .wasInvoked()

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.AlreadyPremium>(it)
        }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun isPremiumExpired() {
        assertEquals(
            appStorage.premiumEndDate.isPassed(),
            viewModel.isPremiumExpired()
        )
        verify(appStorage)
            .invocation { premiumEndDate }
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

    @Test
    fun `isPremiumEverActivated returns false when premiumEndDate is not zero`() {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(1)

        assertFalse { viewModel.isPremiumEverActivated() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun `isPremiumEverActivated returns true when premiumEndDate is zero`() {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        assertTrue { viewModel.isPremiumEverActivated() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun updatePremiumEndDate() = runTest {
        viewModel.state.onSubscription {
            viewModel.updatePremiumEndDate()
        }.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.premiumEndDate.isNotEmpty() }

            verify(appStorage)
                .invocation { premiumEndDate = PremiumType.VIDEO.calculatePremiumEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun getAppTheme() {
        assertEquals(-1, viewModel.getAppTheme()) // already mocked
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onBackClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Back>(it)
        }
    }

    @Test
    fun onCurrenciesClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onCurrenciesClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.OpenCurrencies>(it)
        }
    }

    @Test
    fun onWatchersClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onWatchersClick()
        }.firstOrNull().let {
            assertEquals(SettingsEffect.OpenWatchers, it)
        }
    }

    @Test
    fun onFeedBackClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onFeedBackClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.FeedBack>(it)
        }
    }

    @Test
    fun onShareClick() = runTest {
        val link = "link"

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { link }

        viewModel.effect.onSubscription {
            viewModel.event.onShareClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Share>(it)
            assertEquals(link, it.marketLink)
        }
    }

    @Test
    fun onSupportUsClick() = runTest {
        val link = "link"

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { link }

        viewModel.effect.onSubscription {
            viewModel.event.onSupportUsClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.SupportUs>(it)
            assertEquals(link, it.marketLink)
        }
    }

    @Test
    fun onOnGitHubClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onOnGitHubClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.OnGitHub>(it)
        }
    }

    @Test
    fun onPremiumClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onPremiumClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Premium>(it)
        }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun onThemeClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onThemeClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.ThemeDialog>(it)
        }
    }

    @Test
    fun onSyncClick() = runTest {
        given(currencyDataSource)
            .coroutine { currencyDataSource.getActiveCurrencies() }
            .thenReturn(listOf())

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Synchronising>(it)
        }

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertTrue { viewModel.data.synced }
            assertIs<SettingsEffect.OnlyOneTimeSync>(it)
        }

        verify(analyticsManager)
            .invocation { trackEvent(Event.OfflineSync) }
            .wasInvoked()
    }

    @Test
    fun onPrecisionClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onPrecisionClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.SelectPrecision>(it)
        }
    }

    @Test
    fun onPrecisionSelect() = runTest {
        val value = Random.nextInt()

        viewModel.state.onSubscription {
            viewModel.event.onPrecisionSelect(value)
        }.firstOrNull().let {
            assertNotNull(it)
            assertEquals(value.indexToNumber(), it.precision)

            println("-----")
            verify(calculationStorage)
                .setter(calculationStorage::precision)
                .with(eq(value.indexToNumber()))
                .wasInvoked()
        }
    }
}

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
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.viewmodel.settings.model.PremiumStatus
import com.oztechan.ccc.client.viewmodel.settings.util.indexToNumber
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.Watcher
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
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

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
    private val calculationStorage =
        configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val conversionDataSource = configure(mock(classOf<ConversionDataSource>())) {
        stubsUnitByDefault = true
    }

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager =
        configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

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
    private val shouldShowAds = Random.nextBoolean()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(currencyList))

        every { watcherDataSource.getWatchersFlow() }
            .returns(flowOf(watcherLists))

        every { appConfigRepository.getDeviceType() }
            .returns(Device.IOS)

        every { appConfigRepository.getVersion() }
            .returns(version)

        runTest {
            coEvery { appStorage.getPremiumEndDate() }
                .returns(0)

            coEvery { adControlRepository.shouldShowBannerAd() }
                .returns(shouldShowAds)

            coEvery { calculationStorage.getPrecision() }
                .returns(mockedPrecision)

            coEvery { appStorage.getAppTheme() }
                .returns(-1)
        }
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
            assertEquals(shouldShowAds, it.isBannerAdVisible)
        }

        coVerify { adControlRepository.shouldShowBannerAd() }
            .wasInvoked()
    }

    @Test
    fun `when premiumEndDate is never set PremiumStatus is NeverActivated`() = runTest {
        coEvery { appStorage.getPremiumEndDate() }
            .returns(0)

        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertIs<PremiumStatus.NeverActivated>(it.premiumStatus)
        }
    }

    @Test
    fun `when premiumEndDate is passed PremiumStatus is Expired`() = runTest {
        coEvery { appStorage.getPremiumEndDate() }
            .returns(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertIs<PremiumStatus.Expired>(it.premiumStatus)
        }
    }

    @Test
    fun `when premiumEndDate is not passed PremiumStatus is Active`() = runTest {
        coEvery { appStorage.getPremiumEndDate() }
            .returns(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertIs<PremiumStatus.Active>(it.premiumStatus)
        }
    }

    @Test
    fun `successful synchroniseConversions update the database`() = runTest {
        viewModel.data.synced = false

        val base = "EUR"
        val conversion = Conversion(base)
        val currency = Currency(base, "", "")

        coEvery { currencyDataSource.getActiveCurrencies() }
            .returns(listOf(currency))

        coEvery { backendApiService.getConversion(base) }
            .returns(conversion)

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Synchronising>(it)
        }

        coVerify { conversionDataSource.insertConversion(conversion) }
            .wasInvoked()

        coVerify { backendApiService.getConversion(base) }
            .wasInvoked()
    }

    @Test
    fun `failed synchroniseConversions should pass Synchronised effect`() = runTest {
        viewModel.data.synced = false

        coEvery { currencyDataSource.getActiveCurrencies() }
            .returns(currencyList)

        coEvery { backendApiService.getConversion("") }
            .throws(Exception("test"))

        viewModel.effect.onSubscription {
            viewModel.event.onSyncClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.Synchronising>(it)
        }

        coVerify { conversionDataSource.insertConversion(Conversion()) }
            .wasNotInvoked()
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

        every { appConfigRepository.getMarketLink() }
            .returns(link)

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

        every { appConfigRepository.getMarketLink() }
            .returns(link)

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

        coVerify { appStorage.getPremiumEndDate() }
            .wasInvoked()

        coEvery { appStorage.getPremiumEndDate() }
            .returns(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumClick()
        }.firstOrNull().let {
            assertIs<SettingsEffect.AlreadyPremium>(it)
        }

        coVerify { appStorage.getPremiumEndDate() }
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
        coEvery { currencyDataSource.getActiveCurrencies() }
            .returns(listOf())

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

        verify { analyticsManager.trackEvent(Event.OfflineSync) }
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

            coVerify { calculationStorage.setPrecision(value.indexToNumber()) }
                .wasInvoked()
        }
    }

    @Test
    fun onThemeChange() = runTest {
        val mockTheme = AppTheme.DARK

        viewModel.effect.onSubscription {
            viewModel.onThemeChange(mockTheme)
        }.firstOrNull().let {
            assertEquals(mockTheme, viewModel.state.value.appThemeType)
            assertIs<SettingsEffect.ChangeTheme>(it)
            assertEquals(mockTheme.themeValue, it.themeValue)
        }

        coVerify { appStorage.setAppTheme(mockTheme.themeValue) }
            .wasInvoked()
    }
}

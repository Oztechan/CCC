/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.model.AdConfig
import com.oztechan.ccc.config.model.AppConfig
import com.oztechan.ccc.config.model.AppReview
import com.oztechan.ccc.config.model.AppUpdate
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class MainViewModelTest : BaseViewModelTest() {

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsDataSource, configService, appConfigRepository, adRepository, analyticsManager)
    }

    private val appThemeValue = Random.nextInt()
    private val mockDevice = Device.IOS

    @BeforeTest
    fun setup() {
        given(settingsDataSource)
            .invocation { appTheme }
            .thenReturn(appThemeValue)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { 1L }

        given(appConfigRepository)
            .invocation { getDeviceType() }
            .then { mockDevice }
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.IsAdFree(viewModel.isAdFree().toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.SessionCount(settingsDataSource.sessionCount.toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation {
                setUserProperty(
                    UserProperty.AppTheme(AppTheme.getAnalyticsThemeName(settingsDataSource.appTheme, mockDevice))
                )
            }
            .wasInvoked()
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.DevicePlatform(mockDevice.name)) }
            .wasInvoked()
    }

    // SEED
    @Test
    fun check_state_is_null() {
        assertNull(viewModel.state)
    }

    // public methods
    @Test
    fun isFirstRun() {
        val boolean: Boolean = Random.nextBoolean()

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, viewModel.isFistRun())

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        assertEquals(appThemeValue, viewModel.getAppTheme())

        verify(settingsDataSource)
            .invocation { appTheme }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_true() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() + SECOND }

        assertEquals(true, viewModel.isAdFree())

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_false() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - SECOND }

        assertEquals(false, viewModel.isAdFree())

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    // event
    @Test
    fun onPause() = with(viewModel) {
        event.onPause()
        assertEquals(false, data.adVisibility)
        assertEquals(true, data.adJob.isCancelled)
    }

    @Test
    fun onResume_adjustSessionCount() = with(viewModel) {
        val mockConfig = AppConfig()
        val mockSessionCount = Random.nextLong()

        given(configService)
            .invocation { configService.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(false)

        given(appConfigRepository)
            .invocation { checkAppUpdate(true) }
            .thenReturn(false)

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { "" }

        assertEquals(true, data.isNewSession)

        event.onResume()

        verify(settingsDataSource)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasInvoked()
        assertEquals(false, data.isNewSession)

        event.onResume()

        verify(settingsDataSource)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasNotInvoked()

        assertEquals(false, data.isNewSession)
    }

    @Test
    fun onResume_setupInterstitialAdTimer() = with(viewModel) {
        val mockConfig = AppConfig(
            adConfig = AdConfig(
                interstitialAdInitialDelay = 0L,
                interstitialAdPeriod = 0L
            )
        )
        val mockSessionCount = Random.nextLong()

        given(configService)
            .invocation { configService.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(true)

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - SECOND }

        effect.before {
            onResume()
        }.after {
            assertEquals(true, data.adVisibility)
            assertEquals(true, data.adJob.isActive)

            assertIs<MainEffect.ShowInterstitialAd>(it)

            data.adJob.cancel()
            assertEquals(false, data.adJob.isActive)
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
        verify(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .wasInvoked()
        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun onResume_checkAppUpdate_nothing_happens_when_check_update_returns_null() = with(viewModel) {
        val mockConfig = AppConfig()
        val mockSessionCount = Random.nextLong()

        given(configService)
            .invocation { configService.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        event.onResume()

        assertFalse { data.isAppUpdateShown }

        verify(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun onResume_checkAppUpdate_app_review_should_ask_when_check_update_returns_not_null() {
        val mockSessionCount = Random.nextLong()
        val mockBoolean = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .then { false }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(mockBoolean)

        val mockConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockDevice.name,
                    updateForceVersion = BuildKonfig.versionCode + 1,
                    updateLatestVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configService)
            .invocation { configService.appConfig }
            .then { mockConfig }

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { "" }

        viewModel.effect.before {
            viewModel.onResume()
        }.after {
            assertIs<MainEffect.AppUpdateEffect>(it)
            assertEquals(mockBoolean, it.isCancelable)
            assertTrue { viewModel.data.isAppUpdateShown }
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()

        verify(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun onResume_checkReview_should_request_review_when_shouldShowAppReview_returns_true() =
        with(viewModel) {
            val mockConfig = AppConfig(
                appReview = AppReview(appReviewDialogDelay = 0L)
            )
            val mockSessionCount = Random.nextLong()

            given(adRepository)
                .invocation { shouldShowInterstitialAd() }
                .then { false }

            given(configService)
                .invocation { configService.appConfig }
                .then { mockConfig }

            given(settingsDataSource)
                .invocation { sessionCount }
                .then { mockSessionCount }

            given(appConfigRepository)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(appConfigRepository)
                .invocation { shouldShowAppReview() }
                .then { true }

            effect.before {
                onResume()
            }.after {
                assertIs<MainEffect.RequestReview>(it)
            }

            verify(appConfigRepository)
                .invocation { shouldShowAppReview() }
                .wasInvoked()
            verify(configService)
                .invocation { appConfig }
                .wasInvoked()
        }

    @Test
    fun onResume_checkReview_should_do_nothing_when_shouldShowAppReview_returns_false() =
        with(viewModel) {
            val mockConfig = AppConfig(
                appReview = AppReview(appReviewDialogDelay = 0L)
            )
            val mockSessionCount = Random.nextLong()

            given(configService)
                .invocation { configService.appConfig }
                .then { mockConfig }

            given(settingsDataSource)
                .invocation { sessionCount }
                .then { mockSessionCount }

            given(appConfigRepository)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(appConfigRepository)
                .invocation { shouldShowAppReview() }
                .then { false }

            onResume()

            verify(appConfigRepository)
                .invocation { shouldShowAppReview() }
                .wasInvoked()
        }
}

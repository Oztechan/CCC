/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.github.submob.scopemob.castTo
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.repository.session.SessionRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class MainViewModelTest : BaseViewModelTest() {

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val sessionRepository = mock(classOf<SessionRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsDataSource, configService, sessionRepository, analyticsManager)
    }

    @BeforeTest
    fun setup() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { 1L }
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        given(settingsDataSource)
            .invocation { appTheme }
            .thenReturn(Random.nextInt())

        viewModel // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.IsAdFree(viewModel.isAdFree().toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.SessionCount(settingsDataSource.sessionCount.toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation {
                setUserProperty(UserProperty.AppTheme(AppTheme.getAnalyticsThemeName(settingsDataSource.appTheme)))
            }
            .wasInvoked()
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.DevicePlatform(device.name)) }
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
        val int: Int = Random.nextInt()

        given(settingsDataSource)
            .invocation { appTheme }
            .thenReturn(int)

        assertEquals(int, viewModel.getAppTheme())

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_true() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() + 100 }

        assertEquals(true, viewModel.isAdFree())

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_false() {
        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - 100 }

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

        given(sessionRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(false)

        given(sessionRepository)
            .invocation { checkAppUpdate(true) }
            .thenReturn(false)

        given(sessionRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

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

        given(sessionRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(sessionRepository)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(true)

        given(sessionRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - 1 }

        effect.before {
            onResume()
        }.after {
            assertEquals(true, data.adVisibility)
            assertEquals(true, data.adJob.isActive)

            assertTrue { it is MainEffect.ShowInterstitialAd }

            data.adJob.cancel()
            assertEquals(false, data.adJob.isActive)
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
        verify(sessionRepository)
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

        given(sessionRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(sessionRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        event.onResume()

        assertFalse { data.isAppUpdateShown }

        verify(sessionRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun onResume_checkAppUpdate_app_review_should_ask_when_check_update_returns_not_null() {
        val mockSessionCount = Random.nextLong()
        val mockBoolean = Random.nextBoolean()

        given(sessionRepository)
            .invocation { shouldShowInterstitialAd() }
            .then { false }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(mockBoolean)

        val mockConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = device.name,
                    updateForceVersion = BuildKonfig.versionCode + 1,
                    updateLatestVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configService)
            .invocation { configService.appConfig }
            .then { mockConfig }

        given(sessionRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        viewModel.effect.before {
            viewModel.onResume()
        }.after {
            assertTrue { it is MainEffect.AppUpdateEffect }
            assertTrue { it?.castTo<MainEffect.AppUpdateEffect>()?.isCancelable == mockBoolean }
            assertTrue { viewModel.data.isAppUpdateShown }
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()

        verify(sessionRepository)
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

            given(sessionRepository)
                .invocation { shouldShowInterstitialAd() }
                .then { false }

            given(configService)
                .invocation { configService.appConfig }
                .then { mockConfig }

            given(settingsDataSource)
                .invocation { sessionCount }
                .then { mockSessionCount }

            given(sessionRepository)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(sessionRepository)
                .invocation { shouldShowAppReview() }
                .then { true }

            effect.before {
                onResume()
            }.after {
                assertTrue { it is MainEffect.RequestReview }
            }

            verify(sessionRepository)
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

            given(sessionRepository)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(sessionRepository)
                .invocation { shouldShowAppReview() }
                .then { false }

            onResume()

            verify(sessionRepository)
                .invocation { shouldShowAppReview() }
                .wasInvoked()
        }
}

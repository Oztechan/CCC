/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.github.submob.scopemob.castTo
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.manager.session.SessionManager
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.config.ConfigManager
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
    private val configManager = mock(classOf<ConfigManager>())

    @Mock
    private val sessionManager = mock(classOf<SessionManager>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsDataSource, configManager, sessionManager)
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

    @Test
    fun getSessionCount() {
        val mockSessionCount = Random.nextLong()

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        assertEquals(mockSessionCount, viewModel.getSessionCount())

        verify(settingsDataSource)
            .invocation { sessionCount }
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

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
            .invocation { checkAppUpdate(false) }
            .thenReturn(false)

        given(sessionManager)
            .invocation { checkAppUpdate(true) }
            .thenReturn(false)

        given(sessionManager)
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

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(sessionManager)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(true)

        given(sessionManager)
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

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
        verify(sessionManager)
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

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
            .invocation { checkAppUpdate(false) }
            .thenReturn(null)

        given(sessionManager)
            .invocation { shouldShowAppReview() }
            .then { true }

        event.onResume()

        assertFalse { data.isAppUpdateShown }

        verify(sessionManager)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun onResume_checkAppUpdate_app_review_should_ask_when_check_update_returns_not_null() {
        val mockSessionCount = Random.nextLong()
        val mockBoolean = Random.nextBoolean()

        given(sessionManager)
            .invocation { shouldShowInterstitialAd() }
            .then { false }

        given(settingsDataSource)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
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

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        given(sessionManager)
            .invocation { shouldShowAppReview() }
            .then { true }

        viewModel.effect.before {
            viewModel.onResume()
        }.after {
            assertTrue { it is MainEffect.AppUpdateEffect }
            assertTrue { it?.castTo<MainEffect.AppUpdateEffect>()?.isCancelable == mockBoolean }
            assertTrue { viewModel.data.isAppUpdateShown }
        }

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()

        verify(sessionManager)
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

            given(sessionManager)
                .invocation { shouldShowInterstitialAd() }
                .then { false }

            given(configManager)
                .invocation { configManager.appConfig }
                .then { mockConfig }

            given(settingsDataSource)
                .invocation { sessionCount }
                .then { mockSessionCount }

            given(sessionManager)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(sessionManager)
                .invocation { shouldShowAppReview() }
                .then { true }

            effect.before {
                onResume()
            }.after {
                assertTrue { it is MainEffect.RequestReview }
            }

            verify(sessionManager)
                .invocation { shouldShowAppReview() }
                .wasInvoked()
            verify(configManager)
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

            given(configManager)
                .invocation { configManager.appConfig }
                .then { mockConfig }

            given(settingsDataSource)
                .invocation { sessionCount }
                .then { mockSessionCount }

            given(sessionManager)
                .invocation { checkAppUpdate(false) }
                .thenReturn(null)

            given(sessionManager)
                .invocation { shouldShowAppReview() }
                .then { false }

            onResume()

            verify(sessionManager)
                .invocation { shouldShowAppReview() }
                .wasInvoked()
        }
}

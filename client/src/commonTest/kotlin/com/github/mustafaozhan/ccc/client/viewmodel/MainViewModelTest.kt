/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.helper.SessionManager
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.model.AdConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class MainViewModelTest {

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    @Mock
    private val configManager = mock(classOf<ConfigManager>())

    @Mock
    private val sessionManager = mock(classOf<SessionManager>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsRepository, configManager, sessionManager)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
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

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, viewModel.isFistRun())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        val int: Int = Random.nextInt()

        given(settingsRepository)
            .invocation { appTheme }
            .thenReturn(int)

        assertEquals(int, viewModel.getAppTheme())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_true() {
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .then { nowAsLong() + 1 }

        assertEquals(true, viewModel.isAdFree())

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_false() {
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - 1 }

        assertEquals(false, viewModel.isAdFree())

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun getSessionCount() {
        val mockSessionCount = Random.nextLong()

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        assertEquals(mockSessionCount, viewModel.getSessionCount())

        verify(settingsRepository)
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

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
            .function(sessionManager::checkAppUpdate)
            .whenInvokedWith(any())
            .thenReturn(false)

        given(sessionManager)
            .invocation { shouldShowAppReview() }
            .then { true }

        assertEquals(true, data.isNewSession)

        event.onResume()

        verify(settingsRepository)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasInvoked()
        assertEquals(false, data.isNewSession)

        event.onResume()

        verify(settingsRepository)
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

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(sessionManager)
            .function(sessionManager::checkAppUpdate)
            .whenInvokedWith(any())
            .thenReturn(null)

        given(sessionManager)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(true)

        given(sessionManager)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(settingsRepository)
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
        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }
}

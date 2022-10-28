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
import com.oztechan.ccc.client.storage.AppStorage
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.config.AppConfigService
import com.oztechan.ccc.config.ad.AdConfigService
import com.oztechan.ccc.config.model.AdConfig
import com.oztechan.ccc.config.model.AppConfig
import com.oztechan.ccc.config.model.AppReview
import com.oztechan.ccc.config.model.AppUpdate
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
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

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class MainViewModelTest : BaseViewModelTest<MainViewModel>() {

    override val subject: MainViewModel by lazy {
        MainViewModel(
            appStorage,
            appConfigService,
            appConfigRepository,
            adConfigService,
            adRepository,
            analyticsManager
        )
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @Mock
    private val appConfigService = mock(classOf<AppConfigService>())

    @Mock
    private val adConfigService = mock(classOf<AdConfigService>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    @Mock
    private val analyticsManager = mock(classOf<AnalyticsManager>())

    private val appThemeValue = Random.nextInt()
    private val mockDevice = Device.IOS

    @BeforeTest
    override fun setup() {
        super.setup()

        given(appStorage)
            .invocation { appTheme }
            .thenReturn(appThemeValue)

        given(appStorage)
            .invocation { adFreeEndDate }
            .then { nowAsLong() }

        given(appStorage)
            .invocation { sessionCount }
            .then { 1L }

        given(appConfigRepository)
            .invocation { getDeviceType() }
            .then { mockDevice }

        given(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(false)
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        subject // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.IsAdFree(subject.isAdFree().toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.SessionCount(appStorage.sessionCount.toString())) }
            .wasInvoked()
        verify(analyticsManager)
            .invocation {
                setUserProperty(
                    UserProperty.AppTheme(AppTheme.getAnalyticsThemeName(appStorage.appTheme, mockDevice))
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
        assertNull(subject.state)
    }

    // public methods
    @Test
    fun isFirstRun() {
        val boolean: Boolean = Random.nextBoolean()

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, subject.isFistRun())

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        assertEquals(appThemeValue, subject.getAppTheme())

        verify(appStorage)
            .invocation { appTheme }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_true() {
        given(appStorage)
            .invocation { adFreeEndDate }
            .then { nowAsLong() + SECOND }

        assertTrue { subject.isAdFree() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun isAdFree_for_future_should_return_false() {
        given(appStorage)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - SECOND }

        assertFalse { subject.isAdFree() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    // event
    @Test
    fun onPause() = with(subject) {
        event.onPause()
        assertFalse { data.adVisibility }
        assertTrue { data.adJob.isCancelled }
    }

    @Test
    fun onResume_adjustSessionCount() = with(subject) {
        val mockConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
            listOf()
        )
        val mockSessionCount = Random.nextLong()

        given(appConfigService)
            .invocation { appConfigService.config }
            .then { mockConfig }

        given(adConfigService)
            .invocation { config }
            .then { AdConfig(0, 0, 0L, 0L) }

        given(appStorage)
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

        assertTrue { data.isNewSession }

        event.onResume()

        verify(appStorage)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasInvoked()
        assertFalse { data.isNewSession }

        event.onResume()

        verify(appStorage)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasNotInvoked()

        assertFalse { data.isNewSession }
    }

    @Test
    fun onResume_setupInterstitialAdTimer() = with(subject) {
        val mockConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
            listOf()
        )
        val mockSessionCount = Random.nextLong()

        given(appConfigService)
            .invocation { appConfigService.config }
            .then { mockConfig }

        given(adConfigService)
            .invocation { config }
            .then { AdConfig(0, 0, 0L, 0L) }

        given(appStorage)
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

        given(appStorage)
            .invocation { adFreeEndDate }
            .then { nowAsLong() - SECOND }

        effect.before {
            onResume()
        }.after {
            assertTrue { data.adVisibility }
            assertTrue { data.adJob.isActive }

            assertIs<MainEffect.ShowInterstitialAd>(it)

            data.adJob.cancel()
            assertFalse { data.adJob.isActive }
        }

        verify(appConfigService)
            .invocation { config }
            .wasInvoked()
        verify(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .wasInvoked()
        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()
    }

    @Test
    fun onResume_checkAppUpdate_nothing_happens_when_check_update_returns_null() = with(subject) {
        val mockConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
            listOf()
        )
        val mockSessionCount = Random.nextLong()

        given(appConfigService)
            .invocation { appConfigService.config }
            .then { mockConfig }

        given(appStorage)
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

        given(appStorage)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(mockBoolean)

        val mockConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
            appUpdate = listOf(
                AppUpdate(
                    name = mockDevice.name,
                    updateForceVersion = BuildKonfig.versionCode + 1,
                    updateLatestVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(appConfigService)
            .invocation { appConfigService.config }
            .then { mockConfig }

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(appConfigRepository)
            .invocation { getMarketLink() }
            .then { "" }

        subject.effect.before {
            subject.onResume()
        }.after {
            assertIs<MainEffect.AppUpdateEffect>(it)
            assertEquals(mockBoolean, it.isCancelable)
            assertTrue { subject.data.isAppUpdateShown }
        }

        verify(appConfigService)
            .invocation { config }
            .wasInvoked()

        verify(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun onResume_checkReview_should_request_review_when_shouldShowAppReview_returns_true() =
        with(subject) {
            val mockConfig = AppConfig(
                AdConfig(0, 0, 0L, 0L),
                AppReview(0, 0L),
                listOf()
            )
            val mockSessionCount = Random.nextLong()

            given(appConfigService)
                .invocation { appConfigService.config }
                .then { mockConfig }

            given(appStorage)
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
            verify(appConfigService)
                .invocation { config }
                .wasInvoked()
        }

    @Test
    fun onResume_checkReview_should_do_nothing_when_shouldShowAppReview_returns_false() =
        with(subject) {
            val mockConfig = AppConfig(
                AdConfig(0, 0, 0L, 0L),
                AppReview(0, 0L),
                listOf()
            )
            val mockSessionCount = Random.nextLong()

            given(appConfigService)
                .invocation { appConfigService.config }
                .then { mockConfig }

            given(appStorage)
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

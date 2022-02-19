package com.github.mustafaozhan.ccc.client.helper

import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.util.getRandomDateLong
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.model.AdConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.config.model.AppReview
import com.github.mustafaozhan.config.model.AppUpdate
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
import kotlin.test.assertTrue

class SessionManagerTest {
    @Mock
    private val configManager = mock(classOf<ConfigManager>())

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    private val sessionManager: SessionManagerImpl by lazy {
        SessionManagerImpl(configManager, settingsRepository)
    }

    private val mockSessionCount = Random.nextLong()

    @BeforeTest
    fun setup() {
        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockSessionCount)
    }

    @Test
    fun shouldShowBannerAd() {
        val mockLong = Random.getRandomDateLong()
        val mockBoolean = Random.nextBoolean()
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(
            adConfig = AdConfig(bannerAdSessionCount = mockBannerSessionCount)
        )

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(mockBoolean)

        assertEquals(
            !mockBoolean && mockLong.isRewardExpired() && mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_while_first_run_is_false() {
        val mockLong = Random.getRandomDateLong()
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(
            adConfig = AdConfig(bannerAdSessionCount = mockBannerSessionCount)
        )

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(false)

        assertEquals(
            mockLong.isRewardExpired() && mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_while_first_run_is_false_and_when_reward_expired() {
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(
            adConfig = AdConfig(bannerAdSessionCount = mockBannerSessionCount)
        )

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 1)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(false)

        assertEquals(
            mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowInterstitialAd() {

        val mockInterstitialAdSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(
            adConfig = AdConfig(bannerAdSessionCount = mockInterstitialAdSessionCount)
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(
            mockSessionCount > mockInterstitialAdSessionCount,
            sessionManager.shouldShowInterstitialAd()
        )

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_false_when_force_and_current_version_bigger_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(false, sessionManager.checkAppUpdate(false))

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_true_when_forceVersion_less_than_current_and_updateVersion_bigger_than_current() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode - 1
                )
            )
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(true, sessionManager.checkAppUpdate(false))

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_update_and_force_version_is_less_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode - 1,
                    updateForceVersion = BuildKonfig.versionCode - 1
                )
            )
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, sessionManager.checkAppUpdate(false))

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_device_name_is_different_than_remote() {
        val mockName = "mock"
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, sessionManager.checkAppUpdate(false))

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_it_is_already_shown() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, sessionManager.checkAppUpdate(true))

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_true_when_sessionCount_is_biggerThan_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() + 1)

        assertTrue { sessionManager.shouldShowAppReview() }

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_less_than_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() - 1)

        assertFalse { sessionManager.shouldShowAppReview() }

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_equal_to_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong())

        assertFalse { sessionManager.shouldShowAppReview() }

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }
}

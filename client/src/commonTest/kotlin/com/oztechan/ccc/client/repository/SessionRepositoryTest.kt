package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.repository.session.SessionRepositoryImpl
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionRepositoryTest {
    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    private val repository: SessionRepositoryImpl by lazy {
        SessionRepositoryImpl(configService, settingsDataSource)
    }

    @Test
    fun shouldShowBannerAd_is_false_when_firstRun_and_not_rewardExpired_and_sessionCount_smaller_than_banner_000() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt - 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_not_firstRun_and_not_rewardExpired_and_sessionCount_smaller_than_banner_100() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt - 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_firstRun_and_rewardExpired_and_sessionCount_smaller_than_banner_010() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt - 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_firstRun_and_not_rewardExpired_and_sessionCount_bigger_than_banner_001() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt + 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_firstRun_and_rewardExpired_and_sessionCount_bigger_than_banner_011() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt + 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_not_firstRun_and_not_rewardExpired_and_sessionCount_bigger_than_banner_101() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt + 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_false_when_not_firstRun_and_rewardExpired_and_sessionCount_smaller_than_banner_110() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt - 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_is_true_when_not_firstRun_and_rewardExpired_and_sessionCount_bigger_than_banner_111() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt + 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 100)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { repository.shouldShowBannerAd() }

        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowInterstitialAd_returns_true_when_session_count_bigger_than_remote() {
        val someInt = Random.nextInt()
        val mockAppConfig = AppConfig(adConfig = AdConfig(interstitialAdSessionCount = someInt))

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt.toLong() + 1)

        assertTrue { repository.shouldShowInterstitialAd() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowInterstitialAd_returns_false_when_session_count_smaller_than_remote() {
        val someInt = Random.nextInt()
        val mockAppConfig = AppConfig(adConfig = AdConfig(interstitialAdSessionCount = someInt))

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt.toLong() - 1)

        assertFalse { repository.shouldShowInterstitialAd() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
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

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(false, repository.checkAppUpdate(false))

        verify(configService)
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

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(true, repository.checkAppUpdate(false))

        verify(configService)
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

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(false))

        verify(configService)
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

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(false))

        verify(configService)
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

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(true))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_true_when_sessionCount_is_biggerThan_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() + 1)

        assertTrue { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_less_than_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() - 1)

        assertFalse { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_equal_to_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong())

        assertFalse { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }
}

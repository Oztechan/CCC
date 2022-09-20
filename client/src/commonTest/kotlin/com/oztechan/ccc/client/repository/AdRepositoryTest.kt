package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.ad.AdRepositoryImpl
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.model.AdConfig
import com.oztechan.ccc.config.model.AppConfig
import com.oztechan.ccc.test.BaseSubjectTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
internal class AdRepositoryTest : BaseSubjectTest<AdRepository>() {

    override val subject: AdRepository by lazy {
        AdRepositoryImpl(settingsDataSource, configService, device)
    }

    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    private var device: Device = Device.IOS

    @Test
    fun shouldShowBannerAd_is_false_when_firstRun_and_not_rewardExpired_and_sessionCount_smaller_than_banner_000() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt - 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() + SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() - SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() + SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() - SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() + SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() - SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

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
            .thenReturn(nowAsLong() - SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowBannerAd() }

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

        assertTrue { subject.shouldShowInterstitialAd() }

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

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowRemoveAds_Returns_False_When_Device_Is_Huawei() {
        device = Device.Android.Huawei(1)
        assertFalse { subject.shouldShowRemoveAds() }
    }

    @Test
    fun shouldShowRemoveAds_Returns_True_When_ShouldShowBannerAd_Returns_True() {
        val someInt = Random.nextInt()

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt + 1L)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(configService)
            .invocation { appConfig }
            .then { AppConfig(adConfig = AdConfig(someInt)) }

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowRemoveAds() }
    }

    @Test
    fun shouldShowRemoveAds_Returns_True_When_ShouldShowInterstitialAd_Returns_True() {
        val someInt = Random.nextInt()
        val mockAppConfig = AppConfig(adConfig = AdConfig(interstitialAdSessionCount = someInt))

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { firstRun }
            .then { false }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt.toLong() + 1)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        assertTrue { subject.shouldShowRemoveAds() }
    }

    @Test
    fun shouldShowRemoveAds_Returns_False_When_Should_Show_InterstitialAd_And_ShowShowBannerAd_Returns_False() {
        val someInt = Random.nextInt()
        val mockAppConfig = AppConfig(
            adConfig = AdConfig(
                bannerAdSessionCount = someInt,
                interstitialAdSessionCount = someInt
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(someInt.toLong() - 1)

        given(settingsDataSource)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(settingsDataSource)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowRemoveAds() }
    }
}

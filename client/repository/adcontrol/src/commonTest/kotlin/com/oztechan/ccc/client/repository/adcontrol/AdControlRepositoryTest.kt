package com.oztechan.ccc.client.repository.adcontrol

import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

internal class AdControlRepositoryTest {

    private val subject: AdControlRepository by lazy {
        AdControlRepositoryImpl(appStorage, adConfigService)
    }

    private val adConfigService = mock<AdConfigService>()

    private val appStorage = mock<AppStorage>()

    private var mockedSessionCount = Random.nextInt()

    @BeforeTest
    fun setup() {
        every { adConfigService.config }
            .returns(AdConfig(mockedSessionCount, mockedSessionCount, 0L, 0L))
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun and not premiumExpired and sessionCount smaller than banner 000`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount - 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify(VerifyMode.not) { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount smaller than banner 100`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount - 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount smaller than banner 010`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount - 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify(VerifyMode.not) { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + not premiumExpired + sessionCount bigger than banner 001`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount + 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify(VerifyMode.not) { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount bigger than banner 011`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount + 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify(VerifyMode.not) { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount bigger than banner 101`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount + 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify { appStorage.premiumEndDate }

        verify(VerifyMode.not) { appStorage.sessionCount }

        verify(VerifyMode.not) { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + premiumExpired + sessionCount smaller than banner 110`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount - 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify { appStorage.premiumEndDate }

        verify { appStorage.sessionCount }

        verify { adConfigService.config }
    }

    @Test
    fun `shouldShowBannerAd is true when not firstRun + premiumExpired + sessionCount bigger than banner 111`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount + 1L)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        every { appStorage.firstRun }
            .returns(false)

        assertTrue { subject.shouldShowBannerAd() }

        verify { appStorage.firstRun }

        verify { appStorage.premiumEndDate }

        verify { appStorage.sessionCount }

        verify { adConfigService.config }
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count bigger than remote and premiumNotExpired 01`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() + 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }

        // todo need to fix this
        // verify(VerifyMode.not) { adConfigService.config.interstitialAdSessionCount }

        verify(VerifyMode.not) { appStorage.sessionCount }
    }

    @Test
    fun `shouldShowInterstitialAd returns true when session count bigger than remote and premiumExpired 11`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() + 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertTrue { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }

        verify { adConfigService.config }

        verify { appStorage.sessionCount }
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumExpired 00`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() - 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }

        verify(VerifyMode.not) { adConfigService.config }

        verify(VerifyMode.not) { appStorage.sessionCount }
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumNotExpired 10`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() - 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }

        verify { adConfigService.config }

        verify { appStorage.sessionCount }
    }
}

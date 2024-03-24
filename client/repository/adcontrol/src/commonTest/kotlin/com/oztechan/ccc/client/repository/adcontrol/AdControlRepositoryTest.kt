package com.oztechan.ccc.client.repository.adcontrol

import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
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

    @Mock
    private val adConfigService = mock(classOf<AdConfigService>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private var mockedSessionCount = Random.nextInt()

    @BeforeTest
    fun setup() {
        every { adConfigService.config }
            .returns(AdConfig(mockedSessionCount, mockedSessionCount, 0L, 0L))
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun and not premiumExpired and sessionCount smaller than banner 000`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount - 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(true)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasNotInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount smaller than banner 100`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount - 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(false)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount smaller than banner 010`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount - 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(true)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasNotInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when firstRun + not premiumExpired + sessionCount bigger than banner 001`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount + 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(true)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasNotInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount bigger than banner 011`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount + 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(true)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasNotInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount bigger than banner 101`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount + 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(false)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasInvoked()

            verify { appStorage.sessionCount }
                .wasNotInvoked()

            verify { adConfigService.config }
                .wasNotInvoked()
        }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + premiumExpired + sessionCount smaller than banner 110`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount - 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(false)

            assertFalse { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasInvoked()

            verify { appStorage.sessionCount }
                .wasInvoked()

            verify { adConfigService.config }
                .wasInvoked()
        }

    @Test
    fun `shouldShowBannerAd is true when not firstRun + premiumExpired + sessionCount bigger than banner 111`() =
        runTest {
            every { appStorage.sessionCount }
                .returns(mockedSessionCount + 1L)

            every { appStorage.premiumEndDate }
                .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

            coEvery { appStorage.isFirstRun() }
                .returns(false)

            assertTrue { subject.shouldShowBannerAd() }

            coVerify { appStorage.isFirstRun() }
                .wasInvoked()

            verify { appStorage.premiumEndDate }
                .wasInvoked()

            verify { appStorage.sessionCount }
                .wasInvoked()

            verify { adConfigService.config }
                .wasInvoked()
        }

    @Test
    fun `shouldShowInterstitialAd returns false when session count bigger than remote and premiumNotExpired 01`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() + 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }
            .wasInvoked()

        verify { adConfigService.config.interstitialAdSessionCount }
            .wasNotInvoked()

        verify { appStorage.sessionCount }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns true when session count bigger than remote and premiumExpired 11`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() + 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertTrue { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }
            .wasInvoked()

        verify { adConfigService.config }
            .wasInvoked()

        verify { appStorage.sessionCount }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumExpired 00`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() - 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }
            .wasInvoked()

        verify { adConfigService.config }
            .wasNotInvoked()

        verify { appStorage.sessionCount }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumNotExpired 10`() {
        every { appStorage.sessionCount }
            .returns(mockedSessionCount.toLong() - 1)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify { appStorage.premiumEndDate }
            .wasInvoked()

        verify { adConfigService.config }
            .wasInvoked()

        verify { appStorage.sessionCount }
            .wasInvoked()
    }
}

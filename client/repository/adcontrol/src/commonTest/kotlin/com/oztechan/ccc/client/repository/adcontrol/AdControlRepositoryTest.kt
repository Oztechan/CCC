package com.oztechan.ccc.client.repository.adcontrol

import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
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
        given(adConfigService)
            .invocation { config }
            .thenReturn(AdConfig(mockedSessionCount, mockedSessionCount, 0L, 0L))
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun and not premiumExpired and sessionCount smaller than banner 000`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount smaller than banner 100`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount smaller than banner 010`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + not premiumExpired + sessionCount bigger than banner 001`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount bigger than banner 011`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount bigger than banner 101`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + premiumExpired + sessionCount smaller than banner 110`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is true when not firstRun + premiumExpired + sessionCount bigger than banner 111`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count bigger than remote and premiumNotExpired 01`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() + 1)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config.interstitialAdSessionCount }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns true when session count bigger than remote and premiumExpired 11`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() + 1)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertTrue { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumExpired 00`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() - 1)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasNotInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasNotInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote and premiumNotExpired 10`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() - 1)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - 1.seconds.inWholeMilliseconds)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()
    }
}

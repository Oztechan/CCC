package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.ad.AdRepositoryImpl
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.config.model.AdConfig
import com.oztechan.ccc.config.service.ad.AdConfigService
import com.oztechan.ccc.test.BaseSubjectTest
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

@Suppress("TooManyFunctions")
internal class AdRepositoryTest : BaseSubjectTest<AdRepository>() {

    override val subject: AdRepository by lazy {
        AdRepositoryImpl(appStorage, adConfigService, device)
    }

    @Mock
    private val adConfigService = mock(classOf<AdConfigService>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private var device: Device = Device.IOS

    private var mockedSessionCount = Random.nextInt()

    @BeforeTest
    override fun setup() {
        super.setup()

        given(adConfigService)
            .invocation { config }
            .thenReturn(AdConfig(mockedSessionCount, mockedSessionCount, 0L, 0L))
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun and not rewardExpired and sessionCount smaller than banner 000`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not rewardExpired + sessionCount smaller than banner 100`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + rewardExpired + sessionCount smaller than banner 010`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + not rewardExpired + sessionCount bigger than banner 001`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + rewardExpired + sessionCount bigger than banner 011`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not rewardExpired + sessionCount bigger than banner 101`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + rewardExpired + sessionCount smaller than banner 110`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is true when not firstRun + rewardExpired + sessionCount bigger than banner 111`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns true when session count bigger than remote`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() + 1)

        assertTrue { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() - 1)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowRemoveAds Returns False When Device Is Huawei`() {
        device = Device.Android.Huawei(1)
        assertFalse { subject.shouldShowRemoveAds() }
    }

    @Test
    fun `shouldShowRemoveAds Returns True When ShouldShowBannerAd Returns True`() {
        given(adConfigService)
            .invocation { config }
            .thenReturn(AdConfig(0, mockedSessionCount, 0L, 0L))

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowRemoveAds() }
    }

    @Test
    fun `shouldShowRemoveAds Returns True When ShouldShowInterstitialAd Returns True`() {
        given(adConfigService)
            .invocation { config }
            .thenReturn(AdConfig(mockedSessionCount, 0, 0L, 0L))

        given(appStorage)
            .invocation { firstRun }
            .then { false }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() + 1)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - SECOND)

        assertTrue { subject.shouldShowRemoveAds() }
    }

    @Test
    fun `shouldShowRemoveAds Returns False When Should Show InterstitialAd + ShowShowBannerAd Returns False`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() - 1)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowRemoveAds() }
    }
}

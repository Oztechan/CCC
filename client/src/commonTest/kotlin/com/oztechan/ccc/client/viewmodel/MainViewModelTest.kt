/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig
import com.oztechan.ccc.client.helper.BaseViewModelTest
import com.oztechan.ccc.client.helper.util.after
import com.oztechan.ccc.client.helper.util.before
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.viewmodel.main.MainEffect
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.common.core.infrastructure.util.SECOND
import com.oztechan.ccc.common.core.infrastructure.util.nowAsLong
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
            reviewConfigService,
            appConfigRepository,
            adConfigService,
            adRepository,
            analyticsManager
        )
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @Mock
    private val reviewConfigService = mock(classOf<ReviewConfigService>())

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
            .invocation { premiumEndDate }
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
            .invocation { setUserProperty(UserProperty.IsPremium(subject.isPremium().toString())) }
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
    fun `check state is null`() {
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
    fun `isPremium for future should return true`() {
        given(appStorage)
            .invocation { premiumEndDate }
            .then { nowAsLong() + SECOND }

        assertTrue { subject.isPremium() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun `isPremium for future should return false`() {
        given(appStorage)
            .invocation { premiumEndDate }
            .then { nowAsLong() - SECOND }

        assertFalse { subject.isPremium() }

        verify(appStorage)
            .invocation { premiumEndDate }
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
    fun `onResume adjustSessionCount`() = with(subject) {
        val mockSessionCount = Random.nextLong()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(0, 0L) }

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
    fun `onResume setupInterstitialAdTimer`() = with(subject) {
        val mockSessionCount = Random.nextLong()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(0, 0L) }

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
            .invocation { premiumEndDate }
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

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()

        verify(adRepository)
            .invocation { shouldShowInterstitialAd() }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun `onResume checkAppUpdate nothing happens when check update returns null`() = with(subject) {
        val mockSessionCount = Random.nextLong()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(0, 0L) }

        given(adConfigService)
            .invocation { config }
            .then { AdConfig(0, 0, 0L, 0L) }

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
    fun `onResume checkAppUpdate app review should ask when check update returns not null`() {
        val mockSessionCount = Random.nextLong()
        val mockBoolean = Random.nextBoolean()

        given(appStorage)
            .invocation { sessionCount }
            .then { mockSessionCount }

        given(adConfigService)
            .invocation { config }
            .then { AdConfig(0, 0, 0L, 0L) }

        given(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .thenReturn(mockBoolean)

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(0, 0L) }

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

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()

        verify(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun `onResume checkReview should request review when shouldShowAppReview returns true`() =
        with(subject) {
            val mockSessionCount = Random.nextLong()

            given(reviewConfigService)
                .invocation { config }
                .then { ReviewConfig(0, 0L) }

            given(adConfigService)
                .invocation { config }
                .then { AdConfig(0, 0, 0L, 0L) }

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

            verify(reviewConfigService)
                .invocation { config }
                .wasInvoked()
        }

    @Test
    fun `onResume checkReview should do nothing when shouldShowAppReview returns false`() =
        with(subject) {
            val mockSessionCount = Random.nextLong()

            given(reviewConfigService)
                .invocation { config }
                .then { ReviewConfig(0, 0L) }

            given(adConfigService)
                .invocation { config }
                .then { AdConfig(0, 0, 0L, 0L) }

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

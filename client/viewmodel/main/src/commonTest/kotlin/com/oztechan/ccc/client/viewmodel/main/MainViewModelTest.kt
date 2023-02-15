/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.main

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@Suppress("TooManyFunctions", "OPT_IN_USAGE")
internal class MainViewModelTest {

    private val viewModel: MainViewModel by lazy {
        MainViewModel(
            appStorage,
            reviewConfigService,
            appConfigRepository,
            adConfigService,
            adControlRepository,
            analyticsManager
        )
    }

    @Mock
    private val appStorage = configure(mock(classOf<AppStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val reviewConfigService = mock(classOf<ReviewConfigService>())

    @Mock
    private val adConfigService = mock(classOf<AdConfigService>())

    @Mock
    private val appConfigRepository = mock(classOf<AppConfigRepository>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager = configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

    private val appThemeValue = Random.nextInt()
    private val mockDevice = Device.IOS

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        Dispatchers.setMain(UnconfinedTestDispatcher())

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

        given(adControlRepository)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(false)
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

        verify(analyticsManager)
            .invocation { setUserProperty(UserProperty.IsPremium((!appStorage.premiumEndDate.isItOver()).toString())) }
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
        assertNull(viewModel.state)
    }

    // public methods
    @Test
    fun isFirstRun() {
        val boolean: Boolean = Random.nextBoolean()

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, viewModel.isFistRun())

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        assertEquals(appThemeValue, viewModel.getAppTheme())

        verify(appStorage)
            .invocation { appTheme }
            .wasInvoked()
    }

    // event
    @Test
    fun onPause() = with(viewModel) {
        event.onPause()
        assertFalse { data.adVisibility }
        assertTrue { data.adJob.isCancelled }
    }

    @Test
    fun `onResume adjustSessionCount`() = with(viewModel) {
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
    fun `onResume setupInterstitialAdTimer`() = runTest {
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

        given(adControlRepository)
            .invocation { shouldShowInterstitialAd() }
            .thenReturn(true)

        given(appConfigRepository)
            .invocation { shouldShowAppReview() }
            .then { true }

        given(appStorage)
            .invocation { premiumEndDate }
            .then { nowAsLong() - 1.seconds.inWholeMilliseconds }

        viewModel.effect.onSubscription {
            viewModel.onResume()
        }.firstOrNull { // has to use firstOrNull with true returning lambda for loop
            assertTrue { viewModel.data.adVisibility }
            assertTrue { viewModel.data.adJob.isActive }

            assertIs<MainEffect.ShowInterstitialAd>(it)

            viewModel.data.adJob.cancel()
            assertFalse { viewModel.data.adJob.isActive }
            true
        }

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()

        verify(adControlRepository)
            .invocation { shouldShowInterstitialAd() }
            .wasInvoked()

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun `onResume checkAppUpdate nothing happens when check update returns null`() = with(viewModel) {
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
    fun `onResume checkAppUpdate app review should ask when check update returns not null`() = runTest {
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

        viewModel.effect.onSubscription {
            viewModel.onResume()
        }.firstOrNull().let {
            assertIs<MainEffect.AppUpdateEffect>(it)
            assertEquals(mockBoolean, it.isCancelable)
            assertTrue { viewModel.data.isAppUpdateShown }
        }

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()

        verify(appConfigRepository)
            .invocation { checkAppUpdate(false) }
            .wasInvoked()
    }

    @Test
    fun `onResume checkReview should request review when shouldShowAppReview returns true`() = runTest {
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

        viewModel.effect.onSubscription {
            viewModel.onResume()
        }.firstOrNull().let {
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
        with(viewModel) {
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

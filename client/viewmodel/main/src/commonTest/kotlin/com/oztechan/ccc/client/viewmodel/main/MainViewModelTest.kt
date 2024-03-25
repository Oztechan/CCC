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
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.every
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

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
    private val analyticsManager =
        configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

    private val appThemeValue = Random.nextInt()
    private val mockDevice = Device.IOS
    private val isFirstRun: Boolean = Random.nextBoolean()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { appStorage.appTheme }
            .returns(appThemeValue)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong())

        every { appStorage.sessionCount }
            .returns(1L)

        every { appConfigRepository.getDeviceType() }
            .returns(mockDevice)

        every { adControlRepository.shouldShowInterstitialAd() }
            .returns(false)

        every { appStorage.firstRun }
            .returns(isFirstRun)
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

        verify {
            analyticsManager.setUserProperty(
                UserProperty.IsPremium(
                    appStorage.premiumEndDate.isNotPassed().toString()
                )
            )
        }
            .wasInvoked()
        verify { analyticsManager.setUserProperty(UserProperty.SessionCount(appStorage.sessionCount.toString())) }
            .wasInvoked()
        verify {
            analyticsManager.setUserProperty(
                UserProperty.AppTheme(
                    AppTheme.getAnalyticsThemeName(
                        appStorage.appTheme,
                        mockDevice
                    )
                )
            )
        }
            .wasInvoked()
        verify { analyticsManager.setUserProperty(UserProperty.DevicePlatform(mockDevice.name)) }
            .wasInvoked()
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(isFirstRun, it.shouldOnboardUser)
            assertEquals(appThemeValue, it.appTheme)
        }

        verify { appStorage.firstRun }
            .wasInvoked()

        verify { appStorage.appTheme }
            .wasInvoked()
    }

    @Test
    fun `init updates data correctly`() {
        assertNotNull(viewModel.data)
        assertFalse { viewModel.data.adVisibility }
        assertFalse { viewModel.data.isAppUpdateShown }
        assertTrue { viewModel.data.isNewSession }
        assertNotNull(viewModel.data.adJob)
        assertTrue { viewModel.data.adJob.isActive }
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

        every { reviewConfigService.config }
            .returns(ReviewConfig(0, 0L))

        every { adConfigService.config }
            .returns(AdConfig(0, 0, 0L, 0L))

        every { appStorage.sessionCount }
            .returns(mockSessionCount)

        every { appConfigRepository.checkAppUpdate(false) }
            .returns(false)

        every { appConfigRepository.checkAppUpdate(true) }
            .returns(false)

        every { appConfigRepository.shouldShowAppReview() }
            .returns(true)

        every { appConfigRepository.getMarketLink() }
            .returns("")

        assertTrue { data.isNewSession }

        event.onResume()

        verify { appStorage.sessionCount = mockSessionCount + 1 }
            .wasInvoked()
        assertFalse { data.isNewSession }

        event.onResume()

        verify { appStorage.sessionCount = mockSessionCount + 1 }
            .wasNotInvoked()

        assertFalse { data.isNewSession }
    }

    @Test
    fun `onResume setupInterstitialAdTimer`() = runTest {
        val mockSessionCount = Random.nextLong()

        every { reviewConfigService.config }
            .returns(ReviewConfig(0, 0L))

        every { adConfigService.config }
            .returns(AdConfig(0, 0, 0L, 0L))

        every { appStorage.sessionCount }
            .returns(mockSessionCount)

        every { appConfigRepository.checkAppUpdate(false) }
            .returns(null)

        every { adControlRepository.shouldShowInterstitialAd() }
            .returns(true)

        every { appConfigRepository.shouldShowAppReview() }
            .returns(true)

        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.seconds.inWholeMilliseconds)

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

        verify { reviewConfigService.config }
            .wasInvoked()

        verify { adControlRepository.shouldShowInterstitialAd() }
            .wasInvoked()

        verify { appStorage.premiumEndDate }
            .wasInvoked()
    }

    @Test
    fun `onResume checkAppUpdate nothing happens when check update returns null`() =
        with(viewModel) {
            val mockSessionCount = Random.nextLong()

            every { reviewConfigService.config }
                .returns(ReviewConfig(0, 0L))

            every { adConfigService.config }
                .returns(AdConfig(0, 0, 0L, 0L))

            every { appStorage.sessionCount }
                .returns(mockSessionCount)

            every { appConfigRepository.checkAppUpdate(false) }
                .returns(null)

            every { appConfigRepository.shouldShowAppReview() }
                .returns(true)

            event.onResume()

            assertFalse { data.isAppUpdateShown }

            verify { appConfigRepository.checkAppUpdate(false) }
                .wasInvoked()
        }

    @Test
    fun `onResume checkAppUpdate app review should ask when check update returns not null`() =
        runTest {
            val mockSessionCount = Random.nextLong()
            val mockBoolean = Random.nextBoolean()

            every { appStorage.sessionCount }
                .returns(mockSessionCount)

            every { adConfigService.config }
                .returns(AdConfig(0, 0, 0L, 0L))

            every { appConfigRepository.checkAppUpdate(false) }
                .returns(mockBoolean)

            every { reviewConfigService.config }
                .returns(ReviewConfig(0, 0L))

            every { appConfigRepository.shouldShowAppReview() }
                .returns(true)

            every { appConfigRepository.getMarketLink() }
                .returns("")

            viewModel.effect.onSubscription {
                viewModel.onResume()
            }.firstOrNull().let {
                assertIs<MainEffect.AppUpdateEffect>(it)
                assertEquals(mockBoolean, it.isCancelable)
                assertTrue { viewModel.data.isAppUpdateShown }
            }

            verify { reviewConfigService.config }
                .wasInvoked()

            verify { appConfigRepository.checkAppUpdate(false) }
                .wasInvoked()
        }

    @Test
    fun `onResume checkReview should request review when shouldShowAppReview returns true`() =
        runTest {
            val mockSessionCount = Random.nextLong()

            every { reviewConfigService.config }
                .returns(ReviewConfig(0, 0L))

            every { adConfigService.config }
                .returns(AdConfig(0, 0, 0L, 0L))

            every { appStorage.sessionCount }
                .returns(mockSessionCount)

            every { appConfigRepository.checkAppUpdate(false) }
                .returns(null)

            every { appConfigRepository.shouldShowAppReview() }
                .returns(true)

            viewModel.effect.onSubscription {
                viewModel.onResume()
            }.firstOrNull().let {
                assertIs<MainEffect.RequestReview>(it)
            }

            verify { appConfigRepository.shouldShowAppReview() }
                .wasInvoked()

            verify { reviewConfigService.config }
                .wasInvoked()
        }

    @Test
    fun `onResume checkReview should do nothing when shouldShowAppReview returns false`() =
        with(viewModel) {
            val mockSessionCount = Random.nextLong()

            every { reviewConfigService.config }
                .returns(ReviewConfig(0, 0L))

            every { adConfigService.config }
                .returns(AdConfig(0, 0, 0L, 0L))

            every { appStorage.sessionCount }
                .returns(mockSessionCount)

            every { appConfigRepository.checkAppUpdate(false) }
                .returns(null)

            every { appConfigRepository.shouldShowAppReview() }
                .returns(false)

            onResume()

            verify { appConfigRepository.shouldShowAppReview() }
                .wasInvoked()
        }

    @Test
    fun `onResume updates the latest states`() = runTest {
        every { appConfigRepository.checkAppUpdate(false) }
            .returns(false)

        every { appConfigRepository.shouldShowAppReview() }
            .returns(true)

        every { adConfigService.config }
            .returns(AdConfig(0, 0, 0L, 0L))

        every { appConfigRepository.getMarketLink() }
            .returns("")

        every { reviewConfigService.config }
            .returns(ReviewConfig(0, 0L))

        // init the viewModel
        viewModel

        // different states of what has been emitted
        val newAppThemeValue = appThemeValue + 10
        val newIsFirstRun = isFirstRun.not()

        every { appStorage.appTheme }
            .returns(newAppThemeValue)

        every { appStorage.firstRun }
            .returns(newIsFirstRun)

        viewModel.state
            .onSubscription {
                viewModel.event.onResume()
            }.firstOrNull().let {
                assertNotNull(it)
                assertEquals(newIsFirstRun, it.shouldOnboardUser)
                assertEquals(newAppThemeValue, it.appTheme)
            }

        verify { appStorage.firstRun }
            .wasInvoked()

        verify { appStorage.appTheme }
            .wasInvoked()
    }
}

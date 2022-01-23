/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.getRandomDateLong
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainData.Companion.AD_DELAY_INITIAL
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainData.Companion.AD_DELAY_NORMAL
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.RemoteConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.config.model.Store
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainViewModelTest {

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    @Mock
    private val remoteConfig = mock(classOf<RemoteConfig>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsRepository, remoteConfig)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(settingsRepository)
            .invocation { lastReviewRequest }
            .thenReturn(0)

        given(settingsRepository)
            .setter(settingsRepository::lastReviewRequest)
            .whenInvokedWith(any())
            .thenReturn(Unit)
    }

    @Test
    fun app_review_should_ask_when_device_is_google() {
        val mockConfig = AppConfig(
            stores = listOf(
                Store(
                    name = device.name,
                    updateDialogTitle = "",
                    updateDialogDescription = "",
                    updateForceVersion = BuildKonfig.versionCode + 1,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    storeUrl = ""
                )
            )
        )

        given(remoteConfig)
            .invocation { remoteConfig.appConfig }
            .then { mockConfig }

        if (device == Device.ANDROID.GOOGLE) {
            viewModel.effect.before {
                viewModel.onResume()
            }.after { mainEffect ->
                assertTrue { mainEffect is MainEffect.AppUpdateEffect }
                assertEquals(
                    remoteConfig.appConfig.stores.firstOrNull { it.name == device.name },
                    (mainEffect as MainEffect.AppUpdateEffect).store
                )
            }
        }

        verify(remoteConfig)
            .invocation { appConfig }
            .wasInvoked()
    }

    // SEED
    @Test
    fun check_state_is_null() {
        assertNull(viewModel.state)
    }

    @Test
    fun adDelay_returns_correct_value() {
        viewModel.data.isInitialAd = true
        assertEquals(AD_DELAY_INITIAL, viewModel.data.adDelay)

        viewModel.data.isInitialAd = false
        assertEquals(AD_DELAY_NORMAL, viewModel.data.adDelay)
    }

    // init
    @Test
    fun set_lastReviewRequest_now_if_not_initialised_before() {
        given(settingsRepository)
            .invocation { lastReviewRequest }
            .thenReturn(0)

        verify(settingsRepository)
            .invocation { lastReviewRequest = nowAsLong() }
            .wasInvoked()
    }

    // public methods
    @Test
    fun isFirstRun() {
        val boolean: Boolean = Random.nextBoolean()

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, viewModel.isFistRun())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        val int: Int = Random.nextInt()

        given(settingsRepository)
            .invocation { appTheme }
            .thenReturn(int)

        assertEquals(int, viewModel.getAppTheme())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun isAdFree() {
        val long: Long = Random.getRandomDateLong()

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .then { long }

        assertEquals(!long.isRewardExpired(), viewModel.isAdFree())
        assertEquals(long, settingsRepository.adFreeEndDate)

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun checkReview() {
        if (device == Device.ANDROID.GOOGLE) {
            viewModel.effect.before {
                viewModel.checkReview(0)
            }.after {
                assertTrue { it is MainEffect.RequestReview }

                verify(settingsRepository)
                    .invocation { lastReviewRequest = nowAsLong() }
                    .wasInvoked()
            }
        }
    }

    // event
    @Test
    fun onPause() = with(viewModel) {
        event.onPause()
        assertEquals(false, data.adVisibility)
        assertEquals(true, data.adJob.isCancelled)
    }

    @Test
    fun onResume() = with(viewModel) {
        val mockConfig = AppConfig()
        given(remoteConfig)
            .invocation { remoteConfig.appConfig }
            .then { mockConfig }

        event.onResume()
        if (device is Device.ANDROID.GOOGLE ||
            device is Device.IOS
        ) {
            assertEquals(true, data.adVisibility)
            assertEquals(true, data.adJob.isActive)
        }
    }
}

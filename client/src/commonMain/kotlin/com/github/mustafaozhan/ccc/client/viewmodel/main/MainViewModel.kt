/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainData.Companion.REVIEW_DELAY
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.RemoteConfig
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val remoteConfig: RemoteConfig
) : BaseSEEDViewModel(), MainEvent {
    // region SEED
    override val state: StateFlow<BaseState>? = null

    private val _effect = MutableSharedFlow<MainEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    init {
        if (settingsRepository.lastReviewRequest == 0L) {
            settingsRepository.lastReviewRequest = nowAsLong()
        }
    }

    private fun setupInterstitialAdTimer() {
        data.adVisibility = true

        data.adJob = clientScope.launch {
            delay(data.adDelay)

            while (isActive && !isFistRun()) {
                if (data.adVisibility && !isAdFree()) {
                    _effect.emit(MainEffect.ShowInterstitialAd)
                    data.isInitialAd = false
                }
                delay(data.adDelay)
            }
        }
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun isAdFree() = !settingsRepository.adFreeEndDate.isRewardExpired()

    fun checkReview(delay: Long = REVIEW_DELAY) = clientScope
        .whether { settingsRepository.lastReviewRequest.isWeekPassed() }
        ?.whether { device is Device.ANDROID.GOOGLE }
        ?.launch {
            delay(delay)
            _effect.emit(MainEffect.RequestReview)
            settingsRepository.lastReviewRequest = nowAsLong()
        }

    private fun checkAppUpdate() = remoteConfig.appConfig
        .appUpdate
        .firstOrNull { it.name == device.name }
        ?.whether(
            { device is Device.ANDROID.GOOGLE },
            { updateLatestVersion > BuildKonfig.versionCode }
        )?.let {
            clientScope.launch {
                _effect.emit(MainEffect.AppUpdateEffect(it.updateForceVersion <= BuildKonfig.versionCode))
            }
        }

    // region Event
    override fun onPause() = with(data) {
        Logger.d { "MainViewModel onPause" }
        adJob.cancel()
        adVisibility = false
    }

    override fun onResume() {
        Logger.d { "MainViewModel onResume" }

        if (device is Device.ANDROID.GOOGLE ||
            device is Device.IOS
        ) {
            setupInterstitialAdTimer()
        }

        checkAppUpdate()
    }
    // endregion
}

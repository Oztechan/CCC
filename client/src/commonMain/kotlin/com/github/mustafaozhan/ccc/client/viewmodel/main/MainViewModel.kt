/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.helper.SessionManager
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainData.Companion.REVIEW_DELAY
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val configManager: ConfigManager,
    private val sessionManager: SessionManager
) : BaseSEEDViewModel(), MainEvent {
    // region SEED
    override val state: StateFlow<BaseState>? = null

    private val _effect = MutableSharedFlow<MainEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    private fun setupInterstitialAdTimer() {
        if (device is Device.ANDROID.GOOGLE ||
            device is Device.IOS
        ) {
            data.adVisibility = true

            data.adJob = clientScope.launch {
                delay(configManager.appConfig.adConfig.interstitialAdInitialDelay)

                while (isActive && sessionManager.shouldShowInterstitialAd()) {
                    if (data.adVisibility && !isAdFree()) {
                        _effect.emit(MainEffect.ShowInterstitialAd)
                    }
                    delay(configManager.appConfig.adConfig.interstitialAdPeriod)
                }
            }
        }
    }

    private fun adjustSessionCount() {
        if (data.isNewSession) {
            settingsRepository.sessionCount++
            data.isNewSession = false
        }
    }

    private fun checkAppUpdate() {
        sessionManager.checkAppUpdate(data.isAppUpdateShown)?.let { isCancelable ->
            clientScope.launch {
                _effect.emit(MainEffect.AppUpdateEffect(isCancelable))
                data.isAppUpdateShown = true
            }
        }
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun isAdFree() = !settingsRepository.adFreeEndDate.isRewardExpired()

    fun getSessionCount() = settingsRepository.sessionCount

    fun checkReview(delay: Long = REVIEW_DELAY) = clientScope
        .whether { device is Device.ANDROID.GOOGLE }
        ?.launch {
            delay(delay)
            _effect.emit(MainEffect.RequestReview)
        }

    // region Event
    override fun onPause() {
        Logger.d { "MainViewModel onPause" }
        data.adJob.cancel()
        data.adVisibility = false
    }

    override fun onResume() {
        Logger.d { "MainViewModel onResume" }

        adjustSessionCount()
        setupInterstitialAdTimer()
        checkAppUpdate()
        checkReview()
    }
    // endregion
}

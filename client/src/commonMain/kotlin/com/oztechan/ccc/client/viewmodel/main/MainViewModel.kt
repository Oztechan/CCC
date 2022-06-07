/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.manager.session.SessionManager
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.config.ConfigManager
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

    private fun checkReview() {
        if (sessionManager.shouldShowAppReview()) {
            clientScope.launch {
                delay(configManager.appConfig.appReview.appReviewDialogDelay)
                _effect.emit(MainEffect.RequestReview)
            }
        }
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun isAdFree() = !settingsRepository.adFreeEndDate.isRewardExpired()

    fun getSessionCount() = settingsRepository.sessionCount

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

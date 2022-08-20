/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.repository.session.SessionRepository
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDataSource: SettingsDataSource,
    private val configService: ConfigService,
    private val sessionRepository: SessionRepository,
    analyticsManager: AnalyticsManager,
) : BaseSEEDViewModel(), MainEvent {
    // region SEED
    override val state: StateFlow<BaseState>? = null

    private val _effect = MutableSharedFlow<MainEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    init {
        analyticsManager.setUserProperty(UserProperty.IsAdFree(isAdFree().toString()))
        analyticsManager.setUserProperty(UserProperty.SessionCount(settingsDataSource.sessionCount.toString()))
        analyticsManager.setUserProperty(
            UserProperty.AppTheme(AppTheme.getAnalyticsThemeName(settingsDataSource.appTheme, sessionRepository.device))
        )
        analyticsManager.setUserProperty(UserProperty.DevicePlatform(sessionRepository.device.name))
    }

    private fun setupInterstitialAdTimer() {
        data.adVisibility = true

        data.adJob = viewModelScope.launch {
            delay(configService.appConfig.adConfig.interstitialAdInitialDelay)

            while (isActive && sessionRepository.shouldShowInterstitialAd()) {
                if (data.adVisibility && !isAdFree()) {
                    _effect.emit(MainEffect.ShowInterstitialAd)
                }
                delay(configService.appConfig.adConfig.interstitialAdPeriod)
            }
        }
    }

    private fun adjustSessionCount() {
        if (data.isNewSession) {
            settingsDataSource.sessionCount++
            data.isNewSession = false
        }
    }

    private fun checkAppUpdate() {
        sessionRepository.checkAppUpdate(data.isAppUpdateShown)?.let { isCancelable ->
            viewModelScope.launch {
                _effect.emit(MainEffect.AppUpdateEffect(isCancelable, sessionRepository.device.marketLink))
                data.isAppUpdateShown = true
            }
        }
    }

    private fun checkReview() {
        if (sessionRepository.shouldShowAppReview()) {
            viewModelScope.launch {
                delay(configService.appConfig.appReview.appReviewDialogDelay)
                _effect.emit(MainEffect.RequestReview)
            }
        }
    }

    fun isFistRun() = settingsDataSource.firstRun

    fun getAppTheme() = settingsDataSource.appTheme

    fun isAdFree() = !settingsDataSource.adFreeEndDate.isRewardExpired()

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

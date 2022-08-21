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
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
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
    private val appConfigRepository: AppConfigRepository,
    private val adRepository: AdRepository,
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
        with(analyticsManager) {
            setUserProperty(UserProperty.IsAdFree(isAdFree().toString()))
            setUserProperty(UserProperty.SessionCount(settingsDataSource.sessionCount.toString()))
            setUserProperty(
                UserProperty.AppTheme(
                    AppTheme.getAnalyticsThemeName(
                        settingsDataSource.appTheme,
                        appConfigRepository.getDeviceType()
                    )
                )
            )
            setUserProperty(UserProperty.DevicePlatform(appConfigRepository.getDeviceType().name))
        }
    }

    private fun setupInterstitialAdTimer() {
        data.adVisibility = true

        data.adJob = viewModelScope.launch {
            delay(configService.appConfig.adConfig.interstitialAdInitialDelay)

            while (isActive && adRepository.shouldShowInterstitialAd()) {
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
        appConfigRepository.checkAppUpdate(data.isAppUpdateShown)?.let { isCancelable ->
            viewModelScope.launch {
                _effect.emit(MainEffect.AppUpdateEffect(isCancelable, appConfigRepository.getMarketLink()))
                data.isAppUpdateShown = true
            }
        }
    }

    private fun checkReview() {
        if (appConfigRepository.shouldShowAppReview()) {
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

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.model.AppTheme
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val appStorage: AppStorage,
    private val reviewConfigService: ReviewConfigService,
    private val appConfigRepository: AppConfigRepository,
    private val adConfigService: AdConfigService,
    private val adControlRepository: AdControlRepository,
    analyticsManager: AnalyticsManager,
) : BaseSEEDViewModel<BaseState, MainEffect, MainEvent, MainData>(), MainEvent {
    // region SEED
    override val state: StateFlow<BaseState>? = null

    private val _effect = MutableSharedFlow<MainEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    init {
        with(analyticsManager) {
            setUserProperty(UserProperty.IsPremium(isPremium().toString()))
            setUserProperty(UserProperty.SessionCount(appStorage.sessionCount.toString()))
            setUserProperty(
                UserProperty.AppTheme(
                    AppTheme.getAnalyticsThemeName(
                        appStorage.appTheme,
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
            delay(adConfigService.config.interstitialAdInitialDelay)

            while (isActive && adControlRepository.shouldShowInterstitialAd()) {
                if (data.adVisibility && !isPremium()) {
                    _effect.emit(MainEffect.ShowInterstitialAd)
                }
                delay(adConfigService.config.interstitialAdPeriod)
            }
        }
    }

    private fun adjustSessionCount() {
        if (data.isNewSession) {
            appStorage.sessionCount++
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
                delay(reviewConfigService.config.appReviewDialogDelay)
                _effect.emit(MainEffect.RequestReview)
            }
        }
    }

    fun isFistRun() = appStorage.firstRun

    fun getAppTheme() = appStorage.appTheme

    fun isPremium() = !appStorage.premiumEndDate.isItOver()

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

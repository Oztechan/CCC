/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.main

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.core.viewmodel.util.update
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val appStorage: AppStorage,
    private val reviewConfigService: ReviewConfigService,
    private val appConfigRepository: AppConfigRepository,
    private val adConfigService: AdConfigService,
    private val adControlRepository: AdControlRepository,
    analyticsManager: AnalyticsManager,
) : BaseSEEDViewModel<MainState, MainEffect, MainEvent, MainData>(), MainEvent {
    // region SEED
    private val _state = MutableStateFlow(MainState())
    override val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as MainEvent

    override val data = MainData()
    // endregion

    init {
        viewModelScope.launch {
            _state.update {
                copy(
                    appTheme = appStorage.getAppTheme(),
                    shouldOnboardUser = appStorage.isFirstRun()
                )
            }
            with(analyticsManager) {
                setUserProperty(
                    UserProperty.IsPremium(
                        appStorage.premiumEndDate.isNotPassed().toString()
                    )
                )
                setUserProperty(UserProperty.SessionCount(appStorage.sessionCount.toString()))
                setUserProperty(
                    UserProperty.AppTheme(
                        AppTheme.getAnalyticsThemeName(
                            appStorage.getAppTheme(),
                            appConfigRepository.getDeviceType()
                        )
                    )
                )
                setUserProperty(UserProperty.DevicePlatform(appConfigRepository.getDeviceType().name))
            }
        }
    }

    private fun setupInterstitialAdTimer() {
        data.adVisibility = true

        data.adJob = viewModelScope.launch {
            delay(adConfigService.config.interstitialAdInitialDelay)

            while (isActive && adControlRepository.shouldShowInterstitialAd()) {
                if (data.adVisibility) {
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
                _effect.emit(
                    MainEffect.AppUpdateEffect(
                        isCancelable,
                        appConfigRepository.getMarketLink()
                    )
                )
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

    // region Event
    override fun onPause() {
        Logger.d { "MainViewModel onPause" }
        data.adJob.cancel()
        data.adVisibility = false
    }

    override fun onResume() = viewModelScope.launchIgnored {
        Logger.d { "MainViewModel onResume" }

        _state.update {
            copy(
                shouldOnboardUser = appStorage.isFirstRun(),
                appTheme = appStorage.getAppTheme()
            )
        }

        adjustSessionCount()
        setupInterstitialAdTimer()
        checkAppUpdate()
        checkReview()
    }
    // endregion
}

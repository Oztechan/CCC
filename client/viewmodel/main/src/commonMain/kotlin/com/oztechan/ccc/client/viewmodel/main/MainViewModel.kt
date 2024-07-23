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
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val appStorage: AppStorage,
    private val reviewConfigService: ReviewConfigService,
    private val appConfigRepository: AppConfigRepository,
    private val adConfigService: AdConfigService,
    private val adControlRepository: AdControlRepository,
    analyticsManager: AnalyticsManager,
) : SEEDViewModel<MainState, MainEffect, MainEvent, MainData>(
    initialState = MainState(
        shouldOnboardUser = appStorage.firstRun,
        appTheme = appStorage.appTheme
    ),
    initialData = MainData()
),
    MainEvent {

    init {
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
                if (data.adVisibility) {
                    setEffect { MainEffect.ShowInterstitialAd }
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
                setEffect {
                    MainEffect.AppUpdateEffect(
                        isCancelable,
                        appConfigRepository.getMarketLink()
                    )
                }
                data.isAppUpdateShown = true
            }
        }
    }

    private fun checkReview() {
        if (appConfigRepository.shouldShowAppReview()) {
            viewModelScope.launch {
                delay(reviewConfigService.config.appReviewDialogDelay)
                setEffect { MainEffect.RequestReview }
            }
        }
    }

    // region Event
    override fun onPause() {
        Logger.d { "MainViewModel onPause" }
        data.adJob.cancel()
        data.adVisibility = false
    }

    override fun onResume() {
        Logger.d { "MainViewModel onResume" }

        setState {
            copy(
                shouldOnboardUser = appStorage.firstRun,
                appTheme = appStorage.appTheme
            )
        }

        adjustSessionCount()
        setupInterstitialAdTimer()
        checkAppUpdate()
        checkReview()
    }
    // endregion
}

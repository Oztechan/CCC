package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager

class SessionManager(
    private val configManager: ConfigManager,
    private val settingsRepository: SettingsRepository
) {
    fun shouldShowBannerAd() = !settingsRepository.firstRun &&
        settingsRepository.adFreeEndDate.isRewardExpired() &&
        settingsRepository.sessionCount > configManager.appConfig.adConfig.bannerAdSessionCount

    fun shouldShowInterstitialAd() =
        settingsRepository.sessionCount > configManager.appConfig.adConfig.interstitialAdSessionCount
}

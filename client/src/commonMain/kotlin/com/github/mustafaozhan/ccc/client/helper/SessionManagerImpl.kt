package com.github.mustafaozhan.ccc.client.helper

import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager

class SessionManagerImpl(
    private val configManager: ConfigManager,
    private val settingsRepository: SettingsRepository
) : SessionManager {
    override fun shouldShowBannerAd() = !settingsRepository.firstRun &&
        settingsRepository.adFreeEndDate.isRewardExpired() &&
        settingsRepository.sessionCount > configManager.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        settingsRepository.sessionCount > configManager.appConfig.adConfig.interstitialAdSessionCount
}

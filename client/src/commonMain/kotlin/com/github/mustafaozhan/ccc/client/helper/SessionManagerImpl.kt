package com.github.mustafaozhan.ccc.client.helper

import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.oztechan.ccc.config.ConfigManager

class SessionManagerImpl(
    private val configManager: ConfigManager,
    private val settingsRepository: SettingsRepository
) : SessionManager {
    override fun shouldShowBannerAd() = !settingsRepository.firstRun &&
        settingsRepository.adFreeEndDate.isRewardExpired() &&
        settingsRepository.sessionCount > configManager.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        settingsRepository.sessionCount > configManager.appConfig.adConfig.interstitialAdSessionCount

    override fun checkAppUpdate(
        isAppUpdateShown: Boolean
    ): Boolean? = configManager.appConfig
        .appUpdate
        .firstOrNull { it.name == device.name }
        ?.whether(
            { !isAppUpdateShown },
            { updateLatestVersion > BuildKonfig.versionCode }
        )?.let {
            it.updateForceVersion <= BuildKonfig.versionCode
        }

    override fun shouldShowAppReview(): Boolean = configManager.appConfig
        .appReview
        .whether { settingsRepository.sessionCount > it.appReviewSessionCount }
        ?.mapTo { true }
        ?: false
}

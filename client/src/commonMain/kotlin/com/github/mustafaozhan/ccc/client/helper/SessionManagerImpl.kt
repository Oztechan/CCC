package com.github.mustafaozhan.ccc.client.helper

import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.scopemob.whether

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
            { device is Device.ANDROID.GOOGLE },
            { updateLatestVersion > BuildKonfig.versionCode }
        )?.let {
            it.updateForceVersion <= BuildKonfig.versionCode
        }
}

package com.oztechan.ccc.client.manager.session

import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigManager

class SessionManagerImpl(
    private val configManager: ConfigManager,
    private val settingsDataSource: SettingsDataSource
) : SessionManager {
    override fun shouldShowBannerAd() = !settingsDataSource.firstRun &&
        settingsDataSource.adFreeEndDate.isRewardExpired() &&
        settingsDataSource.sessionCount > configManager.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        settingsDataSource.sessionCount > configManager.appConfig.adConfig.interstitialAdSessionCount

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
        .whether { settingsDataSource.sessionCount > it.appReviewSessionCount }
        ?.mapTo { true }
        ?: false
}

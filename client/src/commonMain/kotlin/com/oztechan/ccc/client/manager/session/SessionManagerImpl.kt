package com.oztechan.ccc.client.manager.session

import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigService

class SessionManagerImpl(
    private val configService: ConfigService,
    private val settingsDataSource: SettingsDataSource
) : SessionManager {
    override fun shouldShowBannerAd() = !settingsDataSource.firstRun &&
        settingsDataSource.adFreeEndDate.isRewardExpired() &&
        settingsDataSource.sessionCount > configService.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        settingsDataSource.sessionCount > configService.appConfig.adConfig.interstitialAdSessionCount

    override fun checkAppUpdate(
        isAppUpdateShown: Boolean
    ): Boolean? = configService.appConfig
        .appUpdate
        .firstOrNull { it.name == device.name }
        ?.whether(
            { !isAppUpdateShown },
            { updateLatestVersion > BuildKonfig.versionCode }
        )?.let {
            it.updateForceVersion <= BuildKonfig.versionCode
        }

    override fun shouldShowAppReview(): Boolean = configService.appConfig
        .appReview
        .whether { settingsDataSource.sessionCount > it.appReviewSessionCount }
        ?.mapTo { true }
        ?: false
}

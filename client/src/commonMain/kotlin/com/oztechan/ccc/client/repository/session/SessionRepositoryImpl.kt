package com.oztechan.ccc.client.repository.session

import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.device
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigService

class SessionRepositoryImpl(
    private val configService: ConfigService,
    private val settingsDataSource: SettingsDataSource
) : SessionRepository {
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

    override fun shouldShowRemoveAds() = when {
        device is Device.ANDROID.Huawei -> false
        shouldShowBannerAd() || shouldShowInterstitialAd() -> true
        else -> false
    }
}

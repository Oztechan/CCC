package com.oztechan.ccc.client.repository.ad

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigService

class AdRepositoryImpl(
    private val settingsDataSource: SettingsDataSource,
    private val configService: ConfigService,
    private val device: Device
) : AdRepository {
    override fun shouldShowBannerAd() = !settingsDataSource.firstRun &&
        settingsDataSource.adFreeEndDate.isRewardExpired() &&
        settingsDataSource.sessionCount > configService.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        settingsDataSource.sessionCount > configService.appConfig.adConfig.interstitialAdSessionCount

    override fun shouldShowRemoveAds() = when {
        device is Device.Android.Huawei -> false
        shouldShowBannerAd() || shouldShowInterstitialAd() -> true
        else -> false
    }
}

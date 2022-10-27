package com.oztechan.ccc.client.repository.ad

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.common.storage.AppStorage
import com.oztechan.ccc.config.ConfigService

internal class AdRepositoryImpl(
    private val appStorage: AppStorage,
    private val configService: ConfigService,
    private val device: Device
) : AdRepository {
    override fun shouldShowBannerAd() = !appStorage.firstRun &&
        appStorage.adFreeEndDate.isRewardExpired() &&
        appStorage.sessionCount > configService.appConfig.adConfig.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        appStorage.sessionCount > configService.appConfig.adConfig.interstitialAdSessionCount

    override fun shouldShowRemoveAds() = when {
        device is Device.Android.Huawei -> false
        shouldShowBannerAd() || shouldShowInterstitialAd() -> true
        else -> false
    }
}

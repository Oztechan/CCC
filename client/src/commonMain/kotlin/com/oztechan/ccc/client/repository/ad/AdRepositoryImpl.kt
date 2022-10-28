package com.oztechan.ccc.client.repository.ad

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.storage.AppStorage
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.config.service.ad.AdConfigService

internal class AdRepositoryImpl(
    private val appStorage: AppStorage,
    private val adConfigService: AdConfigService,
    private val device: Device
) : AdRepository {
    override fun shouldShowBannerAd() = !appStorage.firstRun &&
        appStorage.adFreeEndDate.isRewardExpired() &&
        appStorage.sessionCount > adConfigService.config.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        appStorage.sessionCount > adConfigService.config.interstitialAdSessionCount

    override fun shouldShowRemoveAds() = when {
        device is Device.Android.Huawei -> false
        shouldShowBannerAd() || shouldShowInterstitialAd() -> true
        else -> false
    }
}

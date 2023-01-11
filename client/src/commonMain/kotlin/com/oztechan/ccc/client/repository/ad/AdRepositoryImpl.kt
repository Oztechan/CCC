package com.oztechan.ccc.client.repository.ad

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.util.isRewardExpired
import com.oztechan.ccc.config.service.ad.AdConfigService

internal class AdRepositoryImpl(
    private val appStorage: AppStorage,
    private val adConfigService: AdConfigService
) : AdRepository {
    override fun shouldShowBannerAd() = !appStorage.firstRun &&
        appStorage.adFreeEndDate.isRewardExpired() &&
        appStorage.sessionCount > adConfigService.config.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        appStorage.sessionCount > adConfigService.config.interstitialAdSessionCount
}

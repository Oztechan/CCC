package com.oztechan.ccc.client.repository.ad

import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.storage.app.AppStorage

internal class AdRepositoryImpl(
    private val appStorage: AppStorage,
    private val adConfigService: AdConfigService
) : AdRepository {
    override fun shouldShowBannerAd() = !appStorage.firstRun &&
        appStorage.premiumEndDate.isItOver() &&
        appStorage.sessionCount > adConfigService.config.bannerAdSessionCount

    override fun shouldShowInterstitialAd() =
        appStorage.sessionCount > adConfigService.config.interstitialAdSessionCount
}

package com.oztechan.ccc.client.configservice.ad.mapper

import com.oztechan.ccc.client.configservice.ad.AdConfig as AdConfigRCModel
import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel

internal fun AdConfigRCModel.toAdConfigModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

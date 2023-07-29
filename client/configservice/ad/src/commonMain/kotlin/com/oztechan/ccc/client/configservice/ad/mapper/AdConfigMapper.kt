package com.oztechan.ccc.client.configservice.ad.mapper

import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

internal fun AdConfigRCModel.toAdConfigModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

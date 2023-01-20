package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.model.AdConfig as AdConfigModel
import com.oztechan.ccc.config.service.ad.AdConfig as AdConfigRCModel

internal fun AdConfigRCModel.toAdConfigModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

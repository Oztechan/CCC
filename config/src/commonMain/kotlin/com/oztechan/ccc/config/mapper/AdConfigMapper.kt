package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel
import com.oztechan.ccc.config.model.AdConfig as AdConfigModel

internal fun AdConfigRCModel.toAdConfigModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

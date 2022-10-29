package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.ad.AdConfigEntity
import com.oztechan.ccc.config.model.AdConfig as AdConfigModel

internal fun AdConfigEntity.toModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

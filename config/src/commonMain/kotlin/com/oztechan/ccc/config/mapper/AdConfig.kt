package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.entity.AdConfig as AdConfigEntity
import com.oztechan.ccc.config.model.AdConfig as AdConfigModel

internal fun AdConfigEntity.toModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount,
    interstitialAdSessionCount = interstitialAdSessionCount,
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

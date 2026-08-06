package com.oztechan.ccc.client.configservice.ad.mapper

import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

// Upper-bound safety cap: even if Remote Config ships a mistakenly large value, ads must still
// start for active users. A value this high is never a legitimate warm-up length, only a misconfig.
internal const val MAX_AD_SESSION_COUNT = 100

internal fun AdConfigRCModel.toAdConfigModel() = AdConfigModel(
    bannerAdSessionCount = bannerAdSessionCount.coerceAtMost(MAX_AD_SESSION_COUNT),
    interstitialAdSessionCount = interstitialAdSessionCount.coerceAtMost(MAX_AD_SESSION_COUNT),
    interstitialAdInitialDelay = interstitialAdInitialDelay,
    interstitialAdPeriod = interstitialAdPeriod
)

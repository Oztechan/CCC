package com.oztechan.ccc.client.configservice.ad.model

data class AdConfig(
    val bannerAdSessionCount: Int,
    val interstitialAdSessionCount: Int,
    val interstitialAdInitialDelay: Long,
    val interstitialAdPeriod: Long
)

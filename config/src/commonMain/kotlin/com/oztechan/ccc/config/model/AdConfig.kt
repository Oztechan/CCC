package com.oztechan.ccc.config.model

data class AdConfig(
    val bannerAdSessionCount: Int,
    val interstitialAdSessionCount: Int,
    val interstitialAdInitialDelay: Long,
    val interstitialAdPeriod: Long
)

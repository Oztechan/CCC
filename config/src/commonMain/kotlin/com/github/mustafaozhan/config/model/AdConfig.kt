package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfig(
    @SerialName("is_banner_ad_enabled") val isBannerAdEnabled: Boolean = true,
    @SerialName("interstitial_ad_initial_delay") val interstitialAdInitialDelay: Long = 60000,
    @SerialName("interstitial_ad_period") val interstitialAdPeriod: Long = 180000
)

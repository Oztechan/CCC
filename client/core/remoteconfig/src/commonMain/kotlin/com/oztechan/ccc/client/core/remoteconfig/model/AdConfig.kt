package com.oztechan.ccc.client.core.remoteconfig.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfig(
    @SerialName("banner_ad_session_count") val bannerAdSessionCount: Int = 2,
    @SerialName("interstitial_ad_session_count") val interstitialAdSessionCount: Int = 5,
    @SerialName("interstitial_ad_initial_delay") val interstitialAdInitialDelay: Long = 60000,
    @SerialName("interstitial_ad_period") val interstitialAdPeriod: Long = 180000
)

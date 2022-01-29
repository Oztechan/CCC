package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfig(
    @SerialName("banner_ad_session_count") val bannerAdSessionCount: Int = 2,
    @SerialName("interstitial_ad_session_count") val interstitialAdSessionCount: Int = 5
)

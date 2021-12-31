package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfig(
    @SerialName("is_banner_ad_enabled") val isBannerAdEnabled: Boolean
)

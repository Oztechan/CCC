package com.oztechan.ccc.config.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AppConfig(
    @SerialName("ad_config") val adConfig: AdConfig = AdConfig(),
    @SerialName("app_review") val appReview: AppReview = AppReview(),
    @SerialName("app_update") val appUpdate: List<AppUpdate> = listOf()
)

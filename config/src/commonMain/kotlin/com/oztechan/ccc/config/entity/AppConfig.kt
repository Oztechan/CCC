package com.oztechan.ccc.config.entity

import com.oztechan.ccc.config.ad.AdConfigEntity
import com.oztechan.ccc.config.review.ReviewConfigEntity
import com.oztechan.ccc.config.update.UpdateConfigEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AppConfig(
    @SerialName("ad_config") val adConfig: AdConfigEntity = AdConfigEntity(),
    @SerialName("app_review") val appReview: ReviewConfigEntity = ReviewConfigEntity(),
    @SerialName("app_update") val appUpdate: List<UpdateConfigEntity> = listOf()
)

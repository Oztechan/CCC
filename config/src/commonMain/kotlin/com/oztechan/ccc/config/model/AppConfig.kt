package com.oztechan.ccc.config.model

data class AppConfig(
    val adConfig: AdConfig,
    val appReview: ReviewConfig,
    val appUpdate: List<AppUpdate>
)

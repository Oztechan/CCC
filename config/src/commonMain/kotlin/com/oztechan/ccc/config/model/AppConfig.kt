package com.oztechan.ccc.config.model

data class AppConfig(
    val adConfig: AdConfig,
    val appReview: AppReview,
    val appUpdate: List<AppUpdate>
)

package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppReview(
    @SerialName("app_review_session_count") val appReviewSessionCount: Int = 10,
    @SerialName("app_review_dialog_delay") val appReviewDialogDelay: Long = 20000
)

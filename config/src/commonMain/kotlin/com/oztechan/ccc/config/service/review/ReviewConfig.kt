package com.oztechan.ccc.config.service.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ReviewConfig(
    @SerialName("app_review_session_count") val appReviewSessionCount: Int = 3,
    @SerialName("app_review_dialog_delay") val appReviewDialogDelay: Long = 20000
)

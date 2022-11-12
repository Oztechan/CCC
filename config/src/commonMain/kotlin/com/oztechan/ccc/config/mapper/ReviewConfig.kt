package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.model.ReviewConfig as AppReviewUIModel
import com.oztechan.ccc.config.service.review.ReviewConfigEntity as AppReviewModel

internal fun AppReviewModel.toModel() = AppReviewUIModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

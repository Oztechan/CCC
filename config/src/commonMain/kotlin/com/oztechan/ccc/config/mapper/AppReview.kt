package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.entity.AppReview as AppReviewModel
import com.oztechan.ccc.config.model.AppReview as AppReviewUIModel

internal fun AppReviewModel.toModel() = AppReviewUIModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

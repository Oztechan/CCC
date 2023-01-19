package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.model.ReviewConfig as ReviewConfigModel
import com.oztechan.ccc.config.service.review.ReviewConfig as ReviewConfigRCModel

internal fun ReviewConfigRCModel.toReviewConfigModel() = ReviewConfigModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

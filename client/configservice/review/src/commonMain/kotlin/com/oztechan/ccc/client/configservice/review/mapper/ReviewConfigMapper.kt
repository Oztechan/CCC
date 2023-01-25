package com.oztechan.ccc.client.configservice.review.mapper

import com.oztechan.ccc.client.configservice.review.ReviewConfig as ReviewConfigRCModel
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig as ReviewConfigModel

internal fun ReviewConfigRCModel.toReviewConfigModel() = ReviewConfigModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

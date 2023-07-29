package com.oztechan.ccc.client.configservice.review.mapper

import com.oztechan.ccc.client.configservice.review.model.ReviewConfig as ReviewConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as ReviewConfigRCModel

internal fun ReviewConfigRCModel.toReviewConfigModel() = ReviewConfigModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

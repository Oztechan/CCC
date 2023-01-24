package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as ReviewConfigRCModel
import com.oztechan.ccc.config.model.ReviewConfig as ReviewConfigModel

internal fun ReviewConfigRCModel.toReviewConfigModel() = ReviewConfigModel(
    appReviewSessionCount = appReviewSessionCount,
    appReviewDialogDelay = appReviewDialogDelay,
)

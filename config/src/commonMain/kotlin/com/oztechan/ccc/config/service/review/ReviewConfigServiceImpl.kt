package com.oztechan.ccc.config.service.review

import com.oztechan.ccc.config.mapper.toReviewConfigModel
import com.oztechan.ccc.config.service.BaseConfigService
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.config.model.ReviewConfig as ReviewConfigModel
import com.oztechan.ccc.config.service.review.ReviewConfig as ReviewConfigRCModel

internal class ReviewConfigServiceImpl :
    BaseConfigService<ReviewConfigModel>(
        KEY_AD_CONFIG,
        ReviewConfigRCModel().toReviewConfigModel()
    ),
    ReviewConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<ReviewConfigRCModel>(value)
        .toReviewConfigModel()

    companion object {
        private const val KEY_AD_CONFIG = "review_config"
    }
}

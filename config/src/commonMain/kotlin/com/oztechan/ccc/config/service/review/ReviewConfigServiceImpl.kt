package com.oztechan.ccc.config.service.review

import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.config.mapper.toReviewConfigModel
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as ReviewConfigRCModel
import com.oztechan.ccc.config.model.ReviewConfig as ReviewConfigModel

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

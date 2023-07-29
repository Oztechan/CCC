package com.oztechan.ccc.client.configservice.review

import com.oztechan.ccc.client.configservice.review.mapper.toReviewConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig as ReviewConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as ReviewConfigRCModel

internal class ReviewConfigServiceImpl :
    BaseConfigService<ReviewConfigModel>(
        KEY_AD_CONFIG,
        ReviewConfigRCModel().toReviewConfigModel()
    ),
    ReviewConfigService {

    override fun decode(
        value: String
    ) = value.parseToObject<ReviewConfigRCModel>()
        ?.toReviewConfigModel()
        ?: default

    companion object {
        private const val KEY_AD_CONFIG = "review_config"
    }
}

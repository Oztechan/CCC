package com.oztechan.ccc.client.configservice.review

import com.oztechan.ccc.client.configservice.review.mapper.toReviewConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import kotlinx.coroutines.CoroutineScope
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig as ReviewConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.ReviewConfig as ReviewConfigRCModel

internal class ReviewConfigServiceImpl(
    globalScope: CoroutineScope
) : BaseConfigService<ReviewConfigModel>(
    ReviewConfigRCModel().toReviewConfigModel(),
    KEY_AD_CONFIG,
    globalScope
),
    ReviewConfigService {

    override fun String?.decode() = runCatching {
        parseToObject<ReviewConfigRCModel>().toReviewConfigModel()
    }.getOrElse { default }

    companion object {
        private const val KEY_AD_CONFIG = "review_config"
    }
}

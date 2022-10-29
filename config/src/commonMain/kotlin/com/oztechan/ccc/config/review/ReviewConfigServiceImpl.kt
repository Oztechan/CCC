package com.oztechan.ccc.config.review

import com.oztechan.ccc.config.BaseConfigService
import com.oztechan.ccc.config.mapper.toModel
import com.oztechan.ccc.config.model.ReviewConfig
import kotlinx.serialization.decodeFromString

internal class ReviewConfigServiceImpl :
    BaseConfigService<ReviewConfig>(
        KEY_AD_CONFIG,
        ReviewConfigEntity().toModel()
    ),
    ReviewConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<ReviewConfigEntity>(value)
        .toModel()

    companion object {
        private const val KEY_AD_CONFIG = "review_config"
    }
}

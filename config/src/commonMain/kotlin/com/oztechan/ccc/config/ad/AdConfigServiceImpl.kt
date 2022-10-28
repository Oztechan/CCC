package com.oztechan.ccc.config.ad

import com.oztechan.ccc.config.BaseConfigService
import com.oztechan.ccc.config.mapper.toModel
import com.oztechan.ccc.config.model.AdConfig
import kotlinx.serialization.decodeFromString

internal class AdConfigServiceImpl :
    BaseConfigService<AdConfig>(
        KEY_AD_CONFIG,
        AdConfigEntity().toModel()
    ),
    AdConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<AdConfigEntity>(value)
        .toModel()

    companion object {
        private const val KEY_AD_CONFIG = "ad_config"
    }
}

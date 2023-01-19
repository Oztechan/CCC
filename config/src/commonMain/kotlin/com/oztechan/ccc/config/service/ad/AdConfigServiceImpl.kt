package com.oztechan.ccc.config.service.ad

import com.oztechan.ccc.config.mapper.toAdConfigModel
import com.oztechan.ccc.config.service.BaseConfigService
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.config.model.AdConfig as AdConfigModel
import com.oztechan.ccc.config.service.ad.AdConfig as AdConfigRCModel

internal class AdConfigServiceImpl :
    BaseConfigService<AdConfigModel>(
        KEY_AD_CONFIG,
        AdConfigRCModel().toAdConfigModel()
    ),
    AdConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<AdConfigRCModel>(value)
        .toAdConfigModel()

    companion object {
        private const val KEY_AD_CONFIG = "ad_config"
    }
}

package com.oztechan.ccc.config.service.ad

import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.config.mapper.toAdConfigModel
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel
import com.oztechan.ccc.config.model.AdConfig as AdConfigModel

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

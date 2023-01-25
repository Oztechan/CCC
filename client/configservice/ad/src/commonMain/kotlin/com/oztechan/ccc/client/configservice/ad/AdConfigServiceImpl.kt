package com.oztechan.ccc.client.configservice.ad

import com.oztechan.ccc.client.configservice.ad.mapper.toAdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.oztechan.ccc.client.configservice.ad.AdConfig as AdConfigRCModel
import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel

internal class AdConfigServiceImpl :
    BaseConfigService<AdConfigModel>(
        KEY_AD_CONFIG,
        AdConfigRCModel().toAdConfigModel()
    ),
    AdConfigService {

    private val json = Json { ignoreUnknownKeys = true }

    override fun decode(
        value: String
    ) = json.decodeFromString<AdConfigRCModel>(value)
        .toAdConfigModel()

    companion object {
        private const val KEY_AD_CONFIG = "ad_config"
    }
}

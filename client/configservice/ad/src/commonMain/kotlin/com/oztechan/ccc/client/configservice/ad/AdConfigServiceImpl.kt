package com.oztechan.ccc.client.configservice.ad

import com.oztechan.ccc.client.configservice.ad.mapper.toAdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

internal class AdConfigServiceImpl :
    BaseConfigService<AdConfigModel>(
        KEY_AD_CONFIG,
        AdConfigRCModel().toAdConfigModel()
    ),
    AdConfigService {

    override fun decode(
        value: String
    ) = value.parseToObject<AdConfigRCModel>()
        ?.toAdConfigModel()
        ?: default

    companion object {
        private const val KEY_AD_CONFIG = "ad_config"
    }
}

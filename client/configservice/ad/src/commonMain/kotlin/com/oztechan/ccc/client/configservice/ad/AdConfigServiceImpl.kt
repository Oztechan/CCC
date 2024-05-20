package com.oztechan.ccc.client.configservice.ad

import com.oztechan.ccc.client.configservice.ad.mapper.toAdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import com.oztechan.ccc.client.configservice.ad.model.AdConfig as AdConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

internal class AdConfigServiceImpl :
    BaseConfigService<AdConfigModel>(
        AdConfigRCModel().toAdConfigModel(),
        KEY_AD_CONFIG
    ),
    AdConfigService {

    override fun String?.decode() = runCatching {
        parseToObject<AdConfigRCModel>().toAdConfigModel()
    }.getOrElse { default }

    companion object {
        private const val KEY_AD_CONFIG = "ad_config"
    }
}

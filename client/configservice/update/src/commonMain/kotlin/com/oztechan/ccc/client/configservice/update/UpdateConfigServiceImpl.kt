package com.oztechan.ccc.client.configservice.update

import com.oztechan.ccc.client.configservice.update.mapper.toUpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import com.oztechan.ccc.client.configservice.update.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel

internal class UpdateConfigServiceImpl :
    BaseConfigService<UpdateConfigModel>(
        KEY_AD_CONFIG,
        UpdateConfigRCModel().toUpdateConfigModel()
    ),
    UpdateConfigService {

    override fun decode(
        value: String
    ) = value.parseToObject<UpdateConfigRCModel>()
        ?.toUpdateConfigModel()
        ?: default

    companion object {
        private const val KEY_AD_CONFIG = "update_config"
    }
}

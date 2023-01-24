package com.oztechan.ccc.config.service.update

import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.config.mapper.toUpdateConfigModel
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel
import com.oztechan.ccc.config.model.UpdateConfig as UpdateConfigModel

internal class UpdateConfigServiceImpl :
    BaseConfigService<UpdateConfigModel>(
        KEY_AD_CONFIG,
        UpdateConfigRCModel().toUpdateConfigModel()
    ),
    UpdateConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<UpdateConfigRCModel>(value)
        .toUpdateConfigModel()

    companion object {
        private const val KEY_AD_CONFIG = "update_config"
    }
}

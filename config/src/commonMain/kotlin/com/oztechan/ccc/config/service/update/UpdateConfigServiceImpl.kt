package com.oztechan.ccc.config.service.update

import com.oztechan.ccc.config.mapper.toUpdateConfigModel
import com.oztechan.ccc.config.service.BaseConfigService
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.config.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.config.service.update.UpdateConfig as UpdateConfigRCModel

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

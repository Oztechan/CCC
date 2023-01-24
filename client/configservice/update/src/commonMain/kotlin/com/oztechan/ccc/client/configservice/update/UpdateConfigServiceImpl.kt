package com.oztechan.ccc.client.configservice.update

import com.oztechan.ccc.client.configservice.update.mapper.toUpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.oztechan.ccc.client.configservice.update.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel

internal class UpdateConfigServiceImpl :
    BaseConfigService<UpdateConfigModel>(
        KEY_AD_CONFIG,
        UpdateConfigRCModel().toUpdateConfigModel()
    ),
    UpdateConfigService {

    private val json = Json { ignoreUnknownKeys = true }

    override fun decode(
        value: String
    ) = json.decodeFromString<UpdateConfigRCModel>(value)
        .toUpdateConfigModel()

    companion object {
        private const val KEY_AD_CONFIG = "update_config"
    }
}

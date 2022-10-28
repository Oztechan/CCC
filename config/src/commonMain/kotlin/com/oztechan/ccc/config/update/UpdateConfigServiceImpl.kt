package com.oztechan.ccc.config.update

import com.oztechan.ccc.config.BaseConfigService
import com.oztechan.ccc.config.mapper.toModel
import com.oztechan.ccc.config.model.UpdateConfig
import kotlinx.serialization.decodeFromString

internal class UpdateConfigServiceImpl :
    BaseConfigService<UpdateConfig>(
        KEY_AD_CONFIG,
        UpdateConfigEntity().toModel()
    ),
    UpdateConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<UpdateConfigEntity>(value)
        .toModel()

    companion object {
        private const val KEY_AD_CONFIG = "update_config"
    }
}

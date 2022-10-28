package com.oztechan.ccc.config

import com.oztechan.ccc.config.mapper.toModel
import kotlinx.serialization.decodeFromString
import com.oztechan.ccc.config.entity.AppConfig as AppConfigEntity
import com.oztechan.ccc.config.model.AppConfig as AppConfigModel

internal class AppConfigServiceImpl :
    BaseConfigService<AppConfigModel>(
        KEY_AD_CONFIG,
        AppConfigEntity().toModel()
    ),
    AppConfigService {

    override fun decode(
        value: String
    ) = json.decodeFromString<AppConfigEntity>(value)
        .toModel()

    companion object {
        private const val KEY_AD_CONFIG = "app_config"
    }
}

package com.oztechan.ccc.config

import com.oztechan.ccc.config.model.AppConfig

internal expect class ConfigServiceImpl() : ConfigService {
    override var appConfig: AppConfig
}

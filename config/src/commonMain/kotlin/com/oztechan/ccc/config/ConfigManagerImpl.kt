package com.oztechan.ccc.config

import com.oztechan.ccc.config.model.AppConfig

expect class ConfigManagerImpl() : ConfigManager {
    override var appConfig: AppConfig
}

package com.github.mustafaozhan.config

import com.github.mustafaozhan.config.model.AppConfig

expect class ConfigManagerImpl() : ConfigManager {
    override var appConfig: AppConfig
}

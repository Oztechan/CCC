package com.github.mustafaozhan.config

import com.github.mustafaozhan.config.model.AppConfig

actual class ConfigManagerImpl : ConfigManager {
    actual override var appConfig: AppConfig = AppConfig()
}

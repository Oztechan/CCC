package com.oztechan.ccc.config

import com.oztechan.ccc.config.model.AppConfig

actual class ConfigManagerImpl : ConfigManager {
    actual override var appConfig: AppConfig = AppConfig()
}

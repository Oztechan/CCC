package com.oztechan.ccc.config

import com.oztechan.ccc.config.model.AppConfig

actual class ConfigServiceImpl : ConfigService {
    actual override var appConfig: AppConfig = AppConfig()
}

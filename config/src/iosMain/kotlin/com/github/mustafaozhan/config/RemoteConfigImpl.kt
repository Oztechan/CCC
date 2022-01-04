package com.github.mustafaozhan.config

import com.github.mustafaozhan.config.model.AppConfig

actual class RemoteConfigImpl : RemoteConfig {
    actual override var appConfig: AppConfig = AppConfig()
}

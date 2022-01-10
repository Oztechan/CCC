package com.github.mustafaozhan.config

import com.github.mustafaozhan.config.model.AppConfig

expect class RemoteConfigImpl() : RemoteConfig {
    override var appConfig: AppConfig
}

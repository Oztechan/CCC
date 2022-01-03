package com.github.mustafaozhan.config

import com.github.mustafaozhan.config.model.AppConfig

expect class RemoteConfig() {
    var appConfig: AppConfig
}

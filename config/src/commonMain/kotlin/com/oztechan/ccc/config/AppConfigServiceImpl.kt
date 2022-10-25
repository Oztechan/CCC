package com.oztechan.ccc.config

import com.oztechan.ccc.config.model.AppConfig

internal expect class AppConfigServiceImpl() : AppConfigService {
    override var appConfig: AppConfig
}

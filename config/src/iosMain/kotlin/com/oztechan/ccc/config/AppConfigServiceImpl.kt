package com.oztechan.ccc.config

import com.oztechan.ccc.config.entity.AppConfig
import com.oztechan.ccc.config.mapper.toModel

internal actual class AppConfigServiceImpl : AppConfigService {
    actual override var appConfig = AppConfig().toModel()
}

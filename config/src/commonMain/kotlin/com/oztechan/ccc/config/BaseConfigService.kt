package com.oztechan.ccc.config

import kotlinx.serialization.json.Json

internal expect abstract class BaseConfigService<T>(
    configKey: String,
    default: T
) {
    var appConfig: T

    val json: Json

    abstract fun decode(value: String): T
}

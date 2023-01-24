package com.oztechan.ccc.client.core.remoteconfig

import kotlinx.serialization.json.Json

expect abstract class BaseConfigService<T>(
    configKey: String,
    default: T
) {
    var config: T

    val json: Json

    abstract fun decode(value: String): T
}

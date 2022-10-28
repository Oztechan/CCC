package com.oztechan.ccc.config

import co.touchlab.kermit.Logger
import kotlinx.serialization.json.Json

internal actual abstract class BaseConfigService<T> actual constructor(
    configKey: String,
    default: T
) {
    actual var config: T

    actual abstract fun decode(value: String): T

    actual val json = Json { ignoreUnknownKeys = true }

    init {
        Logger.d { "${this::class.simpleName} init" }
        config = default
    }
}

package com.oztechan.ccc.client.core.remoteconfig

import co.touchlab.kermit.Logger

actual abstract class BaseConfigService<T> actual constructor(
    configKey: String,
    default: T
) {
    actual var config: T

    actual val default: T

    actual abstract fun decode(value: String): T

    init {
        Logger.d { "${this::class.simpleName} init" }
        this.default = default
        config = default
    }
}

package com.oztechan.ccc.client.core.remoteconfig

import co.touchlab.kermit.Logger

actual abstract class BaseConfigService<T> actual constructor(
    default: T,
    configKey: String,
) {

    actual var config: T

    actual val default: T

    actual abstract fun String?.decode(): T

    init {
        Logger.d { "${this::class.simpleName} init" }
        this.default = default
        config = default
    }
}

package com.oztechan.ccc.client.core.remoteconfig

expect abstract class BaseConfigService<T>(
    configKey: String,
    default: T
) {
    var config: T

    val default: T

    abstract fun decode(value: String): T
}

package com.oztechan.ccc.client.core.remoteconfig

expect abstract class BaseConfigService<T>(
    default: T,
    configKey: String
) {
    var config: T
    val default: T
    abstract fun String?.decode(): T
}

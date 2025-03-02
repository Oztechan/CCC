package com.oztechan.ccc.client.core.remoteconfig

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("TooGenericExceptionCaught")
abstract class BaseConfigService<T>(
    val default: T,
    configKey: String,
    globalScope: CoroutineScope
) {
    @Suppress("MemberVisibilityCanBePrivate")
    var config: T

    abstract fun String?.decode(): T

    init {
        Logger.v { "${this::class.simpleName} init" }

        val remoteConfig = Firebase.remoteConfig

        // Get cached value or default
        config = try {
            remoteConfig.getValue(configKey).asString()
                .takeIf { it.isNotEmpty() }
                ?.decode()
                ?: default
        } catch (e: Exception) {
            Logger.v { "${this::class.simpleName} Remote config not fetched, using cached value: ${e.message}" }
            default
        }

        globalScope.launch {
            try {
                remoteConfig.fetchAndActivate()
                Logger.v { "${this@BaseConfigService::class.simpleName} Remote config updated from server \n $config" }
                config = remoteConfig.getValue(configKey).asString().decode()
            } catch (e: Exception) {
                Logger.v {
                    "${this@BaseConfigService::class.simpleName}" +
                        " Remote config not updated, using cached value: ${e.message}"
                }
            }
        }
    }
}

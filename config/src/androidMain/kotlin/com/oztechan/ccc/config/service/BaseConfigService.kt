package com.oztechan.ccc.config.service

import co.touchlab.kermit.Logger
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.oztechan.ccc.config.BuildConfig
import kotlinx.serialization.json.Json

internal actual abstract class BaseConfigService<T>
actual constructor(
    configKey: String,
    default: T
) {
    actual var config: T

    actual abstract fun decode(value: String): T

    actual val json = Json { ignoreUnknownKeys = true }

    init {
        Logger.d { "${this::class.simpleName} init" }

        Firebase.remoteConfig.apply {

            // get cache
            config = updateConfig(getString(configKey), default)

            setConfigSettingsAsync(
                remoteConfigSettings {
                    if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 0
                }
            )

            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.i("${this::class.simpleName} Remote config updated from server")
                    // get remote
                    config = updateConfig(getString(configKey), default)
                    // cache
                    setDefaultsAsync(mapOf(configKey to config))
                } else {
                    Logger.i("${this::class.simpleName} Remote config is not updated, using cached value")
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun updateConfig(value: String, default: T): T = try {
        decode(value)
    } catch (exception: Exception) {
        Logger.e(exception) { "${this::class.simpleName} Remote config is not updated, using default" }
        default
    }
}

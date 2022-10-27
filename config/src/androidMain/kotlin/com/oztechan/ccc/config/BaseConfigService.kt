package com.oztechan.ccc.config

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.serialization.json.Json

internal actual abstract class BaseConfigService<T>
actual constructor(
    configKey: String,
    default: T
) {
    actual var appConfig: T

    actual abstract fun decode(value: String): T

    actual val json = Json { ignoreUnknownKeys = true }

    init {
        Logger.d { "${this::class.simpleName} init" }

        Firebase.remoteConfig.apply {

            appConfig = updateConfig(getString(configKey), default) // get cached

            setConfigSettingsAsync(
                remoteConfigSettings {
                    if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 0
                }
            )

            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.i("Remote config updated from server")
                    appConfig = updateConfig(getString(configKey), default) // get remote
                    setDefaultsAsync(mapOf(configKey to appConfig)) // cache
                } else {
                    Logger.i("Remote config is not updated, using defaults")
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun updateConfig(value: String, default: T): T = try {
        decode(value)
    } catch (exception: Exception) {
        Logger.e(exception)
        default
    }
}

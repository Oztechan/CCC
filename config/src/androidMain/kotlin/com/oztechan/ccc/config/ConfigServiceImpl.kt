package com.oztechan.ccc.config

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.oztechan.ccc.config.entity.AppConfig
import com.oztechan.ccc.config.mapper.toModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal actual class ConfigServiceImpl : ConfigService {
    actual override var appConfig = AppConfig().toModel()

    init {
        Logger.i { "ConfigServiceImpl init" }

        Firebase.remoteConfig.apply {

            @Suppress("SwallowedException", "TooGenericExceptionCaught")
            try {
                appConfig = parseAppConfig(get(KEY_APP_CONFIG).asString()).toModel()
            } catch (e: Exception) {
                Logger.i { "No cached appConfig available" }
            }

            setConfigSettingsAsync(
                remoteConfigSettings {
                    if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 0
                }
            )

            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.i("Remote config updated from server")

                    @Suppress("TooGenericExceptionCaught")
                    try {
                        appConfig = parseAppConfig(getString(KEY_APP_CONFIG)).toModel()
                        setDefaultsAsync(mapOf(KEY_APP_CONFIG to appConfig))
                    } catch (exception: Exception) {
                        Logger.e(exception)
                    }
                } else {
                    Logger.i("Remote config is not updated, using defaults")
                }
            }
        }
    }

    private fun parseAppConfig(value: String): AppConfig {
        val format = Json { ignoreUnknownKeys = true }
        return format.decodeFromString(value)
    }

    companion object {
        private const val KEY_APP_CONFIG = "app_config"
    }
}

package com.github.mustafaozhan.config

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.config.model.AppConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

actual class RemoteConfigImpl : RemoteConfig {
    actual override var appConfig = AppConfig()

    init {
        Logger.i { "RemoteConfig init" }

        Firebase.remoteConfig.apply {

            appConfig = parseAppConfig(get(KEY_APP_CONFIG).asString())

            setConfigSettingsAsync(
                remoteConfigSettings {
                    if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 0
                }
            )

            @Suppress("TooGenericExceptionCaught")
            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.i("Remote config updated from server")
                } else {
                    Logger.w("Remote config updated, using defaults")
                }

                try {
                    appConfig = parseAppConfig(getString(KEY_APP_CONFIG))
                    setDefaultsAsync(mapOf(KEY_APP_CONFIG to appConfig))
                } catch (exception: Exception) {
                    Logger.e(exception) { exception.message.toString() }
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

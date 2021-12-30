package com.github.mustafaozhan.config

import android.content.Context
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.config.model.AppConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.internal.DefaultsXmlParser
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class RemoteConfig(context: Context) {
    lateinit var appConfig: AppConfig

    init {
        Logger.i { "RemoteConfig init" }

        val defaultMap = DefaultsXmlParser.getDefaultsFromXml(context, R.xml.default_remote_config)

        defaultMap?.get(KEY_APP_CONFIG)?.let {
            appConfig = Json.decodeFromString(it)
        } ?: throw IllegalStateException("No default remote config provided")

        fetchFromRemote()
    }

    private fun fetchFromRemote() = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            remoteConfigSettings {
                if (BuildConfig.DEBUG) minimumFetchIntervalInSeconds = 0
            }
        )

        setDefaultsAsync(R.xml.default_remote_config)

        @Suppress("TooGenericExceptionCaught")
        fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                Logger.i("Remote config updated from server")
            } else {
                Logger.w("Remote config updated, using defaults")
            }

            try {
                val format = Json { ignoreUnknownKeys = true }
                appConfig = format.decodeFromString(getString(KEY_APP_CONFIG))
            } catch (exception: Exception) {
                Logger.e(exception) { exception.message.toString() }
            }
        }
    }

    companion object {
        private const val KEY_APP_CONFIG = "app_config"
    }
}

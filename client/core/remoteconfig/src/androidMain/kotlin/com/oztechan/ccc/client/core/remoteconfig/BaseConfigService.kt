package com.oztechan.ccc.client.core.remoteconfig

import co.touchlab.kermit.Logger
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig

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

        Firebase.remoteConfig.apply {

            // get cache or default
            config = getString(configKey)
                .takeIf { it.isNotEmpty() }
                ?.let { it.decode() }
                ?: default

            setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().build())

            fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    Logger.i("${this::class.simpleName} Remote config updated from server")
                    // get remote
                    config = getString(configKey).decode()
                    // cache
                    setDefaultsAsync(mapOf(configKey to config))
                } else {
                    Logger.i("${this::class.simpleName} Remote config is not updated, using cached value")
                }
            }
        }
    }
}

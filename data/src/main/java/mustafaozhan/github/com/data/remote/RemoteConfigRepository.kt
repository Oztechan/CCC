/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.remote

import com.github.mustafaozhan.data.BuildConfig
import com.github.mustafaozhan.scopemob.whether
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import mustafaozhan.github.com.data.model.RemoteConfig
import timber.log.Timber
import java.io.EOFException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor() {
    companion object {
        private const val CHECK_DURATION: Long = 6
        private const val CHECK_INTERVAL: Long = 4200
        private const val KEY_REMOTE_CONFIG = "remote_config"
    }

    fun checkRemoteConfig() = flow {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings
                    .Builder()
                    .setMinimumFetchIntervalInSeconds(CHECK_INTERVAL)
                    .build()
            )
            fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(CHECK_DURATION))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activate()
                        try {
                            Moshi.Builder().build().adapter(RemoteConfig::class.java)
                                .fromJson(getString(KEY_REMOTE_CONFIG))
                                ?.whether { latestVersion > BuildConfig.VERSION_CODE }
                                ?.let {
                                    GlobalScope.launch {
                                        emit(it)
                                    }
                                }
                        } catch (e: JsonDataException) {
                            Timber.w(e)
                        } catch (e: JsonEncodingException) {
                            Timber.e(e)
                        } catch (e: EOFException) {
                            Timber.e(e, "check remote config file")
                        }
                    }
                }
        }
    }
}

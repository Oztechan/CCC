/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.github.mustafaozhan.basemob.util.showDialog
import com.github.mustafaozhan.scopemob.whether
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.RemoteConfig
import timber.log.Timber
import java.io.EOFException
import java.util.concurrent.TimeUnit

const val CHECK_DURATION: Long = 6
const val CHECK_INTERVAL: Long = 4200
const val KEY_REMOTE_CONFIG = "remote_config"

@Suppress("ComplexMethod")
fun checkRemoteConfig(activity: Activity) {

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
                            ?.apply {
                                showDialog(
                                    activity,
                                    title,
                                    description,
                                    activity.getString(R.string.update),
                                    forceVersion <= BuildConfig.VERSION_CODE
                                ) {
                                    startActivity(activity, Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)), null)
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

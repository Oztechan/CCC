package mustafaozhan.github.com.mycurrencies.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.function.scope.whether
import mustafaozhan.github.com.mycurrencies.model.RemoteConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val CHECK_DURATION: Long = 6
const val CHECK_INTERVAL: Long = 4200
const val REMOTE_CONFIG = "remote_config"

@Suppress("ComplexMethod")
fun checkRemoteConfig(context: Context) {

    val defaultMap = HashMap<String, Any>()
    defaultMap[REMOTE_CONFIG] = RemoteConfig(
        context.getString(R.string.remote_config_title),
        context.getString(R.string.remote_config_description),
        context.getString(R.string.app_market_link)
    )

    FirebaseRemoteConfig.getInstance().apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings
                .Builder()
                .setMinimumFetchIntervalInSeconds(CHECK_INTERVAL)
                .build()
        )
        setDefaultsAsync(defaultMap)
        fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(CHECK_DURATION))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activate()

                    val remoteConfigStr = getString(REMOTE_CONFIG)
                        .whether { isEmpty() }
                        ?.let { defaultMap[REMOTE_CONFIG] as? String }
                        ?: run { getString(REMOTE_CONFIG) }

                    try {
                        Moshi.Builder().build().adapter(RemoteConfig::class.java)
                            .fromJson(remoteConfigStr)
                            ?.whether { latestVersion > BuildConfig.VERSION_CODE }
                            ?.apply {
                                showDialog(
                                    context,
                                    title,
                                    description,
                                    context.getString(R.string.update),
                                    forceVersion <= BuildConfig.VERSION_CODE
                                ) {
                                    startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)), null)
                                }
                            }
                    } catch (e: JsonDataException) {
                        Timber.w(e)
                    } catch (e: JsonEncodingException) {
                        Timber.e(e)
                    }
                }
            }
    }
}

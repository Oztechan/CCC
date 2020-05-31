/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.scopemob.whether
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.RemoteConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    private val _effect = MutableSingleLiveData<MainEffect>()
    val effect: SingleLiveData<MainEffect> = _effect

    fun updateAdFreeActivation() {
        preferencesRepository.adFreeActivatedDate = System.currentTimeMillis()
    }

    fun isRewardExpired() = preferencesRepository.isRewardExpired()

    fun isFirstRun() = preferencesRepository.firstRun

    fun checkRemoteConfig(remoteConfig: RemoteConfig) {
        val defaultMap = HashMap<String, Any>()
        defaultMap[MainData.REMOTE_CONFIG] = remoteConfig

        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings
                    .Builder()
                    .setMinimumFetchIntervalInSeconds(MainData.CHECK_INTERVAL)
                    .build()
            )
            setDefaultsAsync(defaultMap)
            fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(MainData.CHECK_DURATION))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activate()

                        val remoteConfigStr = getString(MainData.REMOTE_CONFIG)
                            .whether { isEmpty() }
                            ?.let { defaultMap[MainData.REMOTE_CONFIG] as? String }
                            ?: run { getString(MainData.REMOTE_CONFIG) }

                        try {
                            Moshi.Builder().build().adapter(RemoteConfig::class.java)
                                .fromJson(remoteConfigStr)
                                ?.whether { latestVersion < BuildConfig.VERSION_CODE }
                                ?.let {
                                    _effect.value = AppUpdateEffect(it)
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
}

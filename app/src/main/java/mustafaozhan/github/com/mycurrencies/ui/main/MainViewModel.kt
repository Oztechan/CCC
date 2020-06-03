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
import java.io.EOFException
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    private val _effect = MutableSingleLiveData<MainEffect>()
    val effect: SingleLiveData<MainEffect> = _effect

    fun updateAdFreeActivation() = preferencesRepository.setAdFreeActivation()

    fun isRewardExpired() = preferencesRepository.isRewardExpired()

    fun isFirstRun() = preferencesRepository.firstRun

    fun checkRemoteConfig() {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings
                    .Builder()
                    .setMinimumFetchIntervalInSeconds(MainData.CHECK_INTERVAL)
                    .build()
            )
            fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(MainData.CHECK_DURATION))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activate()
                        try {
                            Moshi.Builder().build().adapter(RemoteConfig::class.java)
                                .fromJson(getString(MainData.KEY_REMOTE_CONFIG))
                                ?.whether { latestVersion > BuildConfig.VERSION_CODE }
                                ?.let {
                                    _effect.postValue(AppUpdateEffect(it))
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

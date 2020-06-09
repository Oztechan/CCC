/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.model.BaseData
import com.github.mustafaozhan.basemob.model.BaseEffect
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.RemoteConfig

sealed class MainEffect : BaseEffect()
data class AppUpdateEffect(val remoteConfig: RemoteConfig) : MainEffect()

open class MainData(
    private val preferencesRepository: PreferencesRepository
) : BaseData() {

    companion object {
        internal const val MINIMUM_ACTIVE_CURRENCY = 2
        internal const val BACK_DELAY: Long = 2000
        internal const val AD_INITIAL_DELAY: Long = 45000
        internal const val AD_PERIOD: Long = 180000
        internal const val TEXT_EMAIL_TYPE = "text/email"
        internal const val CHECK_DURATION: Long = 6
        internal const val CHECK_INTERVAL: Long = 4200
        internal const val KEY_REMOTE_CONFIG = "remote_config"
        internal const val KEY_BASE_CURRENCY = "base_currency"
    }

    val isRewardExpired
        get() = preferencesRepository.isRewardExpired()

    var firstRun
        get() = preferencesRepository.firstRun
        set(value) {
            preferencesRepository.firstRun = value
        }

    var currentBase
        get() = preferencesRepository.currentBase
        set(value) {
            preferencesRepository.currentBase = value
        }

    fun updateAdFreeActivation() = preferencesRepository.setAdFreeActivation()
}

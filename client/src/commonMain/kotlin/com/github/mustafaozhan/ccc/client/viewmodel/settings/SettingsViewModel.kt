/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.settings

import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions")
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) : BaseSEEDViewModel(), SettingsEvent {
    // region SEED
    private val _state = MutableStateFlow(SettingsState())
    override val state = _state.asStateFlow()

    override val event = this as SettingsEvent

    private val _effect = MutableSharedFlow<SettingsEffect>()
    override val effect = _effect.asSharedFlow()

    override val data = SettingsData()
    // endregion

    init {
        kermit.d { "SettingsViewModel init" }

        _state.update(
            appThemeType = AppTheme.getThemeByValue(settingsRepository.appTheme)
                ?: AppTheme.SYSTEM_DEFAULT,
            addFreeEndDate = settingsRepository.adFreeEndDate.toDateString()
        )

        currencyRepository.collectActiveCurrencies()
            .onEach {
                _state.update(activeCurrencyCount = it.size)
            }.launchIn(clientScope)
    }

    private suspend fun synchroniseRates() {
        _state.update(loading = true)

        _effect.emit(SettingsEffect.Synchronising)

        currencyRepository.getActiveCurrencies()
            .forEach { (name) ->
                delay(SYNC_DELAY)

                apiRepository.getRatesViaBackend(name).execute(
                    success = { offlineRatesRepository.insertOfflineRates(it.toRates()) },
                    error = { error -> kermit.e(error) { error.message.toString() } }
                )
            }

        _effect.emit(SettingsEffect.Synchronised)

        _state.update(loading = false)
        data.synced = true
    }

    fun updateTheme(theme: AppTheme) = clientScope.launchIgnored {
        _state.update(appThemeType = theme)
        settingsRepository.appTheme = theme.themeValue
        _effect.emit(SettingsEffect.ChangeTheme(theme.themeValue))
    }

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    fun isAdFreeNeverActivated() = settingsRepository.adFreeEndDate == 0.toLong()

    fun getAppTheme() = settingsRepository.appTheme

    // used in ios
    @Suppress("unused")
    fun updateAddFreeDate() = RemoveAdType.VIDEO.calculateAdRewardEnd(nowAsLong()).let {
        settingsRepository.adFreeEndDate = it
        _state.update(addFreeEndDate = it.toDateString())
    }

    override fun onCleared() {
        kermit.d { "SettingsViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onBackClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onBackClick" }
        _effect.emit(SettingsEffect.Back)
    }

    override fun onCurrenciesClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onCurrenciesClick" }
        _effect.emit(SettingsEffect.OpenCurrencies)
    }

    override fun onFeedBackClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onFeedBackClick" }
        _effect.emit(SettingsEffect.FeedBack)
    }

    override fun onShareClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onShareClick" }
        _effect.emit(SettingsEffect.Share)
    }

    override fun onSupportUsClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onSupportUsClick" }
        _effect.emit(SettingsEffect.SupportUs)
    }

    override fun onOnGitHubClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onOnGitHubClick" }
        _effect.emit(SettingsEffect.OnGitHub)
    }

    override fun onRemoveAdsClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onRemoveAdsClick" }
        _effect.emit(if (isRewardExpired()) SettingsEffect.RemoveAds else SettingsEffect.AlreadyAdFree)
    }

    override fun onThemeClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onThemeClick" }
        _effect.emit(SettingsEffect.ThemeDialog)
    }

    override fun onSyncClick() = clientScope.launchIgnored {
        kermit.d { "SettingsViewModel onSyncClick" }
        if (data.synced) {
            _effect.emit(SettingsEffect.OnlyOneTimeSync)
        } else {
            synchroniseRates()
        }
    }
    // endregion
}

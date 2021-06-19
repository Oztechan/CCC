/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.settings

import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.model.toModelList
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsData.Companion.SYNC_DELAY
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsState.Companion.update
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
            .mapToModel()
            .onEach {
                _state.update(activeCurrencyCount = it.size)
            }.launchIn(clientScope)
    }

    private suspend fun synchroniseRates() {
        _state.update(loading = true)

        _effect.emit(SettingsEffect.Synchronising)
        currencyRepository.getActiveCurrencies()
            .toModelList()
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

    fun updateTheme(theme: AppTheme) = clientScope.launch {
        _state.update(appThemeType = theme)
        settingsRepository.appTheme = theme.themeValue
        _effect.emit(SettingsEffect.ChangeTheme(theme.themeValue))
    }.toUnit()

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    fun isAdFreeNeverActivated() = settingsRepository.adFreeEndDate == 0.toLong()

    fun getAppTheme() = settingsRepository.appTheme

    override fun onCleared() {
        kermit.d { "SettingsViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onBackClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onBackClick" }
        _effect.emit(SettingsEffect.Back)
    }.toUnit()

    override fun onCurrenciesClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onCurrenciesClick" }
        _effect.emit(SettingsEffect.OpenCurrencies)
    }.toUnit()

    override fun onFeedBackClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onFeedBackClick" }
        _effect.emit(SettingsEffect.FeedBack)
    }.toUnit()

    override fun onShareClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onShareClick" }
        _effect.emit(SettingsEffect.Share)
    }.toUnit()

    override fun onSupportUsClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onSupportUsClick" }
        _effect.emit(SettingsEffect.SupportUs)
    }.toUnit()

    override fun onOnGitHubClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onOnGitHubClick" }
        _effect.emit(SettingsEffect.OnGitHub)
    }.toUnit()

    override fun onRemoveAdsClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onRemoveAdsClick" }
        _effect.emit(if (isRewardExpired()) SettingsEffect.RemoveAds else SettingsEffect.AlreadyAdFree)
    }.toUnit()

    override fun onThemeClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onThemeClick" }
        _effect.emit(SettingsEffect.ThemeDialog)
    }.toUnit()

    override fun onSyncClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onSyncClick" }
        if (data.synced) {
            _effect.emit(SettingsEffect.OnlyOneTimeSync)
        } else {
            synchroniseRates()
        }
    }.toUnit()
    // endregion
}

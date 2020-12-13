/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.model.AppTheme
import com.github.mustafaozhan.ccc.android.util.DAY
import com.github.mustafaozhan.ccc.android.util.dateStringToFormattedString
import com.github.mustafaozhan.ccc.android.util.isRewardExpired
import com.github.mustafaozhan.ccc.android.util.toRates
import com.github.mustafaozhan.ccc.android.util.toUnit
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.kermit
import java.util.Date
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Suppress("TooManyFunctions")
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : ViewModel(), SettingsEvent {

    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }

    // region SEED
    private val _state = MutableSettingsState()
    val state = SettingsState(_state)

    private val _effect = BroadcastChannel<SettingsEffect>(Channel.BUFFERED)
    val effect = _effect.asFlow()

    private val data = SettingsData()

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        _state._appThemeType.value = AppTheme.getThemeByValue(settingsRepository.appTheme)
            ?: AppTheme.SYSTEM_DEFAULT
        _state._addFreeDate.value = Date(
            settingsRepository.adFreeActivatedDate + DAY
        ).dateStringToFormattedString()

        viewModelScope.launch {
            currencyDao.collectActiveCurrencies()
                .collect {
                    _state._activeCurrencyCount.value = it.filter { currency ->
                        currency.isActive
                    }.size
                }
        }
    }

    fun updateAddFreeDate() = Clock.System.now().toEpochMilliseconds().let {
        _state._addFreeDate.value = Date(it + DAY).dateStringToFormattedString()
        settingsRepository.adFreeActivatedDate = it
    }

    fun updateTheme(theme: AppTheme) {
        _state._appThemeType.value = theme
        settingsRepository.appTheme = theme.themeValue
        viewModelScope.launch {
            _effect.send(ChangeThemeEffect(theme.themeValue))
        }
    }

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun getAdFreeActivatedDate() = settingsRepository.adFreeActivatedDate

    fun getAppTheme() = settingsRepository.appTheme

    // region Event
    override fun onBackClick() = viewModelScope.launch {
        _effect.send(BackEffect)
    }.toUnit()

    override fun onCurrenciesClick() = viewModelScope.launch {
        _effect.send(CurrenciesEffect)
    }.toUnit()

    override fun onFeedBackClick() = viewModelScope.launch {
        _effect.send(FeedBackEffect)
    }.toUnit()

    override fun onShareClick() = viewModelScope.launch {
        _effect.send(ShareEffect)
    }.toUnit()

    override fun onSupportUsClick() = viewModelScope.launch {
        _effect.send(SupportUsEffect)
    }.toUnit()

    override fun onOnGitHubClick() = viewModelScope.launch {
        _effect.send(OnGitHubEffect)
    }.toUnit()

    override fun onRemoveAdsClick() = viewModelScope.launch {
        _effect.send(RemoveAdsEffect)
    }.toUnit()

    override fun onThemeClick() = viewModelScope.launch {
        _effect.send(ThemeDialogEffect)
    }.toUnit()

    override fun onSyncClick() {

        viewModelScope.launch {
            if (!data.synced) {
                currencyDao.getActiveCurrencies().forEach { (name) ->
                    delay(SYNC_DELAY)

                    apiRepository.getRatesByBaseViaBackend(name).execute({
                        viewModelScope.launch {
                            offlineRatesDao.insertOfflineRates(it.toRates())
                        }
                    }, { error -> kermit.e(error) { error.message.toString() } })
                }

                data.synced = true
                _effect.send(SynchronisedEffect)
            } else {
                _effect.send(OnlyOneTimeSyncEffect)
            }
        }
    }
    // endregion
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.settings

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.model.toModelList
import com.github.mustafaozhan.ccc.client.util.DAY
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("TooManyFunctions")
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseViewModel(), SettingsEvent {

    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }

    // region SEED
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    private val _effect = BroadcastChannel<SettingsEffect>(Channel.BUFFERED)
    val effect = _effect.asFlow()

    private val data = SettingsData()

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        kermit.d { "SettingsViewModel init" }

        _state.value = _state.value.copy(
            appThemeType = AppTheme.getThemeByValue(settingsRepository.appTheme)
                ?: AppTheme.SYSTEM_DEFAULT,
            addFreeDate = Instant.fromEpochMilliseconds(
                settingsRepository.adFreeActivatedDate + DAY
            ).formatToString()
        )

        clientScope.launch {
            currencyDao.collectActiveCurrencies()
                .mapToModel()
                .collect {
                    _state.value = _state.value.copy(
                        activeCurrencyCount = it.size
                    )
                }
        }
    }

    fun updateAddFreeDate() = Clock.System.now().toEpochMilliseconds().let {
        _state.value = _state.value.copy(
            addFreeDate = Instant.fromEpochMilliseconds(it + DAY).formatToString()
        )
        settingsRepository.adFreeActivatedDate = it
    }

    fun updateTheme(theme: AppTheme) = clientScope.launch {
        _state.value = _state.value.copy(appThemeType = theme)
        settingsRepository.appTheme = theme.themeValue
        _effect.send(ChangeThemeEffect(theme.themeValue))
    }.toUnit()

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun getAdFreeActivatedDate() = settingsRepository.adFreeActivatedDate

    fun getAppTheme() = settingsRepository.appTheme

    override fun onCleared() {
        kermit.d { "SettingsViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onBackClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onBackClick" }
        _effect.send(BackEffect)
    }.toUnit()

    override fun onCurrenciesClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onCurrenciesClick" }
        _effect.send(CurrenciesEffect)
    }.toUnit()

    override fun onFeedBackClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onFeedBackClick" }
        _effect.send(FeedBackEffect)
    }.toUnit()

    override fun onShareClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onShareClick" }
        _effect.send(ShareEffect)
    }.toUnit()

    override fun onSupportUsClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onSupportUsClick" }
        _effect.send(SupportUsEffect)
    }.toUnit()

    override fun onOnGitHubClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onOnGitHubClick" }
        _effect.send(OnGitHubEffect)
    }.toUnit()

    override fun onRemoveAdsClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onRemoveAdsClick" }
        _effect.send(RemoveAdsEffect)
    }.toUnit()

    override fun onThemeClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onThemeClick" }
        _effect.send(ThemeDialogEffect)
    }.toUnit()

    override fun onSyncClick() = clientScope.launch {
        kermit.d { "SettingsViewModel onSyncClick" }

        if (!data.synced) {
            currencyDao.getActiveCurrencies()
                .toModelList()
                .forEach { (name) ->
                    delay(SYNC_DELAY)

                    apiRepository.getRatesByBaseViaBackend(name).execute({
                        clientScope.launch {
                            offlineRatesDao.insertOfflineRates(it.toRates())
                        }
                    }, { error -> kermit.e(error) { error.message.toString() } })
                }

            data.synced = true
            _effect.send(SynchronisedEffect)
        } else {
            _effect.send(OnlyOneTimeSyncEffect)
        }
    }.toUnit()
    // endregion
}

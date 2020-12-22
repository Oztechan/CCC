/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.settings

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.AppTheme
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
        _state._addFreeDate.value = Instant.fromEpochMilliseconds(
            settingsRepository.adFreeActivatedDate + DAY
        ).formatToString()

        clientScope.launch {
            currencyDao.collectActiveCurrencies()
                .collect {
                    _state._activeCurrencyCount.value = it.filter { currency ->
                        currency.isActive
                    }.size
                }
        }
    }

    fun updateAddFreeDate() = Clock.System.now().toEpochMilliseconds().let {
        _state._addFreeDate.value = Instant.fromEpochMilliseconds(it + DAY).formatToString()
        settingsRepository.adFreeActivatedDate = it
    }

    fun updateTheme(theme: AppTheme) {
        _state._appThemeType.value = theme
        settingsRepository.appTheme = theme.themeValue
        clientScope.launch {
            _effect.send(ChangeThemeEffect(theme.themeValue))
        }
    }

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun getAdFreeActivatedDate() = settingsRepository.adFreeActivatedDate

    fun getAppTheme() = settingsRepository.appTheme

    // region Event
    override fun onBackClick() = clientScope.launch {
        _effect.send(BackEffect)
    }.toUnit()

    override fun onCurrenciesClick() = clientScope.launch {
        _effect.send(CurrenciesEffect)
    }.toUnit()

    override fun onFeedBackClick() = clientScope.launch {
        _effect.send(FeedBackEffect)
    }.toUnit()

    override fun onShareClick() = clientScope.launch {
        _effect.send(ShareEffect)
    }.toUnit()

    override fun onSupportUsClick() = clientScope.launch {
        _effect.send(SupportUsEffect)
    }.toUnit()

    override fun onOnGitHubClick() = clientScope.launch {
        _effect.send(OnGitHubEffect)
    }.toUnit()

    override fun onRemoveAdsClick() = clientScope.launch {
        _effect.send(RemoveAdsEffect)
    }.toUnit()

    override fun onThemeClick() = clientScope.launch {
        _effect.send(ThemeDialogEffect)
    }.toUnit()

    override fun onSyncClick() {

        clientScope.launch {
            if (!data.synced) {
                currencyDao.getActiveCurrencies().forEach { (name) ->
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
        }
    }
    // endregion
}

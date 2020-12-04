/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.model.AppTheme
import com.github.mustafaozhan.ccc.android.util.DAY
import com.github.mustafaozhan.ccc.android.util.MutableSingleLiveData
import com.github.mustafaozhan.ccc.android.util.SingleLiveData
import com.github.mustafaozhan.ccc.android.util.isRewardExpired
import com.github.mustafaozhan.ccc.android.util.toRates
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.db.OfflineRatesDao
import com.github.mustafaozhan.data.util.dateStringToFormattedString
import com.github.mustafaozhan.data.util.toRateV2
import java.util.Date
import kotlinx.coroutines.delay
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

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    private val data = SettingsData()

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        _state._appThemeType.value = AppTheme.getThemeByValue(settingsRepository.appTheme)
        _state._addFreeDate.value = Date(
            settingsRepository.adFreeActivatedDate + DAY
        ).dateStringToFormattedString()

        viewModelScope.launch {
            currencyDao.collectActiveCurrencies()
                .collect {
                    _state._activeCurrencyCount.value = it?.filter { currency ->
                        currency.isActive
                    }?.size ?: 0
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
        _effect.postValue(ChangeThemeEffect(theme.themeValue))
    }

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun getAdFreeActivatedDate() = settingsRepository.adFreeActivatedDate

    fun getAppTheme() = settingsRepository.appTheme

    // region Event
    override fun onBackClick() = _effect.postValue(BackEffect)

    override fun onCurrenciesClick() = _effect.postValue(CurrenciesEffect)

    override fun onFeedBackClick() = _effect.postValue(FeedBackEffect)

    override fun onShareClick() = _effect.postValue(ShareEffect)

    override fun onSupportUsClick() = _effect.postValue(SupportUsEffect)

    override fun onOnGitHubClick() = _effect.postValue(OnGitHubEffect)

    override fun onRemoveAdsClick() = _effect.postValue(RemoveAdsEffect)

    override fun onThemeClick() = _effect.postValue(ThemeDialogEffect)

    override fun onSyncClick() {
        if (!data.synced) {
            viewModelScope.launch {
                currencyDao.getActiveCurrencies()?.forEach { (name) ->
                    delay(SYNC_DELAY)
                    apiRepository.getRatesByBase(name).execute(
                        {
                            viewModelScope.launch {
                                offlineRatesDao.insertOfflineRates(
                                    it.toRateV2().toRates()
                                )
                            }
                        },
                        { error -> kermit.e(error) { error.message.toString() } }
                    )
                }
                data.synced = true
                _effect.postValue(SynchronisedEffect)
            }
        } else _effect.postValue(OnlyOneTimeSyncEffect)
    }

    // endregion
}

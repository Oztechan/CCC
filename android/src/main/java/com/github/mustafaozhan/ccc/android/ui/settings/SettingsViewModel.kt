/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.main.model.AppTheme
import com.github.mustafaozhan.ccc.android.ui.main.MainData.Companion.DAY
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsData.Companion.SYNC_DELAY
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.data.api.ApiRepository
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.db.OfflineRatesDao
import com.github.mustafaozhan.data.model.MutableSingleLiveData
import com.github.mustafaozhan.data.model.SingleLiveData
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.data.util.dateStringToFormattedString
import com.github.mustafaozhan.data.util.toRate
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class SettingsViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : ViewModel(), SettingsEvent {

    // region SEED
    private val _state = MutableSettingsState()
    val state = SettingsState(_state)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData(preferencesRepository)

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        _state._appThemeType.value = AppTheme.getThemeByValue(data.appTheme)
        _state._addFreeDate.value =
            Date(preferencesRepository.adFreeActivatedDate).dateStringToFormattedString()

        viewModelScope.launch {
            currencyDao.collectActiveCurrencies()
                .collect {
                    _state._activeCurrencyCount.value = it?.filter { currency ->
                        currency.isActive
                    }?.size ?: 0
                }
        }
    }

    fun updateAddFreeDate() = System.currentTimeMillis().let {
        _state._addFreeDate.value = Date(it + DAY).dateStringToFormattedString()
        data.adFreeActivatedDate = it
    }

    fun updateTheme(theme: AppTheme) {
        _state._appThemeType.value = theme
        data.appTheme = theme.themeValue
        _effect.postValue(ChangeThemeEffect(theme.themeValue))
    }

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
                        { viewModelScope.launch { offlineRatesDao.insertOfflineRates(it.toRate()) } },
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

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val currencyDao: CurrencyDao
) : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _state = MutableSettingsState()
    val state = SettingsState(_state)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData(preferencesRepository)

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        viewModelScope.launch {
            currencyDao.getActiveCurrencies()
                .collect {
                    _state._activeCurrencyCount.value = it?.filter { currency ->
                        currency.isActive
                    }?.size ?: 0
                }
        }
    }

    // region Event
    override fun onBackClick() = _effect.postValue(BackEffect)

    override fun onCurrenciesClick() = _effect.postValue(CurrenciesEffect)

    override fun onFeedBackClick() = _effect.postValue(FeedBackEffect)

    override fun onSupportUsClick() = _effect.postValue(SupportUsEffect)

    override fun onOnGitHubClick() = _effect.postValue(OnGitHubEffect)

    override fun onRemoveAdsClick() = _effect.postValue(RemoveAdsEffect)
    // endregion
}

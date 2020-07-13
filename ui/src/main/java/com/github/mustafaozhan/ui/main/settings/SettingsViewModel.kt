/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository
) : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData(preferencesRepository)

    fun getEvent() = this as SettingsEvent
    // endregion

    // region Event
    override fun onBackClick() = _effect.postValue(BackEffect)

    override fun onCurrenciesClick() = _effect.postValue(CurrenciesEffect)

    override fun onFeedBackClick() = _effect.postValue(FeedBackEffect)

    override fun onSupportUsClick() = _effect.postValue(SupportUsEffect)

    override fun onOnGitHubClick() = _effect.postValue(OnGitHubEffect)

    override fun onRemoveAdsClick() = _effect.postValue(RemoveAdsEffect)
    // endregion
}

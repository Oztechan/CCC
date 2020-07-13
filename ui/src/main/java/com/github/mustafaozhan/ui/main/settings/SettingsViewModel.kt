/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel

class SettingsViewModel : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    fun getEvent() = this as SettingsEvent
    // endregion

    // region Event
    override fun onBackClick() = _effect.postValue(BackEffect)

    override fun onCurrenciesClick() = _effect.postValue(CurrenciesEffect)

    override fun onFeedBackClick() = _effect.postValue(FeedBackEffect)

    override fun onSupportUsClick() = _effect.postValue(SupportUsEffect)

    override fun onOnGitHubClick() = _effect.postValue(OnGitHubEffect)
    // endregion
}

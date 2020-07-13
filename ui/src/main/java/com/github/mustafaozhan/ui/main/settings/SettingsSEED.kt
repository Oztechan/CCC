/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.model.MutableBaseState
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData
import com.github.mustafaozhan.ui.main.model.AppTheme

// State
@Suppress("ConstructorParameterNaming")
data class SettingsState(
    private val _state: MutableSettingsState
) : BaseState() {
    val activeCurrencyCount: LiveData<Int> = _state._activeCurrencyCount
    val appThemeType: LiveData<AppTheme> = _state._appThemeType
}

@Suppress("ConstructorParameterNaming")
data class MutableSettingsState(
    val _activeCurrencyCount: MutableLiveData<Int> = MutableLiveData(0),
    val _appThemeType: MutableLiveData<AppTheme> = MutableLiveData()
) : MutableBaseState()

// Event
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onFeedBackClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
    fun onRemoveAdsClick()
    fun onThemeClick()
}

// Effect
sealed class SettingsEffect : BaseEffect()
object BackEffect : SettingsEffect()
object CurrenciesEffect : SettingsEffect()
object FeedBackEffect : SettingsEffect()
object SupportUsEffect : SettingsEffect()
object OnGitHubEffect : SettingsEffect()
object RemoveAdsEffect : SettingsEffect()
object ThemeDialogEffect : SettingsEffect()
data class ChangeThemeEffect(val themeValue: Int) : SettingsEffect()

// Data
data class SettingsData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository)

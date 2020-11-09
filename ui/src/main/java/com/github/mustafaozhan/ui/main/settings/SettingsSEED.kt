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
    val addFreeDate: LiveData<String> = _state._addFreeDate
}

@Suppress("ConstructorParameterNaming")
data class MutableSettingsState(
    val _activeCurrencyCount: MutableLiveData<Int> = MutableLiveData(0),
    val _appThemeType: MutableLiveData<AppTheme> = MutableLiveData(),
    val _addFreeDate: MutableLiveData<String> = MutableLiveData()
) : MutableBaseState()

// Event
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onFeedBackClick()
    fun onShareClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
    fun onRemoveAdsClick()
    fun onSyncClick()
    fun onThemeClick()
}

// Effect
sealed class SettingsEffect : BaseEffect()
object BackEffect : SettingsEffect()
object CurrenciesEffect : SettingsEffect()
object FeedBackEffect : SettingsEffect()
object ShareEffect : SettingsEffect()
object SupportUsEffect : SettingsEffect()
object OnGitHubEffect : SettingsEffect()
object RemoveAdsEffect : SettingsEffect()
object ThemeDialogEffect : SettingsEffect()
object SynchronisedEffect : SettingsEffect()
object OnlyOneTimeSyncEffect : SettingsEffect()
data class ChangeThemeEffect(val themeValue: Int) : SettingsEffect()

// Data
data class SettingsData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {
    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }

    var synced = false
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.settings

import com.github.mustafaozhan.ccc.android.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State
@Suppress("ConstructorParameterNaming")
data class SettingsState(
    private val _state: MutableSettingsState
) {
    val activeCurrencyCount: StateFlow<Int> = _state._activeCurrencyCount
    val appThemeType: StateFlow<AppTheme> = _state._appThemeType
    val addFreeDate: StateFlow<String> = _state._addFreeDate
}

@Suppress("ConstructorParameterNaming")
data class MutableSettingsState(
    val _activeCurrencyCount: MutableStateFlow<Int> = MutableStateFlow(0),
    val _appThemeType: MutableStateFlow<AppTheme> = MutableStateFlow(AppTheme.SYSTEM_DEFAULT),
    val _addFreeDate: MutableStateFlow<String> = MutableStateFlow("")
)

// Event
interface SettingsEvent {
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
sealed class SettingsEffect
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
data class SettingsData(var synced: Boolean = false)

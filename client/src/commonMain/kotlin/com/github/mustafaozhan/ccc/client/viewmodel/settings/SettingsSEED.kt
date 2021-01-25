/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.settings

import com.github.mustafaozhan.ccc.client.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class SettingsState(
    val activeCurrencyCount: Int = 0,
    val appThemeType: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val addFreeDate: String = ""
) {
    constructor() : this(0, AppTheme.SYSTEM_DEFAULT, "")

    companion object {
        fun MutableStateFlow<SettingsState>.update(
            activeCurrencyCount: Int = value.activeCurrencyCount,
            appThemeType: AppTheme = value.appThemeType,
            addFreeDate: String = value.addFreeDate
        ) {
            value = value.copy(
                activeCurrencyCount = activeCurrencyCount,
                appThemeType = appThemeType,
                addFreeDate = addFreeDate
            )
        }
    }
}

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
sealed class SettingsEffect {
    object Back : SettingsEffect()
    object OpenCurrencies : SettingsEffect()
    object FeedBack : SettingsEffect()
    object Share : SettingsEffect()
    object SupportUs : SettingsEffect()
    object OnGitHub : SettingsEffect()
    object RemoveAds : SettingsEffect()
    object ThemeDialog : SettingsEffect()
    object Synchronised : SettingsEffect()
    object OnlyOneTimeSync : SettingsEffect()
    data class ChangeTheme(val themeValue: Int) : SettingsEffect()
}

// Data
data class SettingsData(var synced: Boolean = false)

package com.oztechan.ccc.client.viewmodel.settings

import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class SettingsState(
    val activeCurrencyCount: Int = 0,
    val activeWatcherCount: Int = 0,
    val appThemeType: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val addFreeEndDate: String = "",
    val loading: Boolean = false
) : BaseState()

// Event
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onWatchersClicked()
    fun onFeedBackClick()
    fun onShareClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
    fun onRemoveAdsClick()
    fun onSyncClick()
    fun onThemeClick()
}

// Effect
sealed class SettingsEffect : BaseEffect() {
    object Back : SettingsEffect()
    object OpenCurrencies : SettingsEffect()
    object OpenWatchers : SettingsEffect()
    object FeedBack : SettingsEffect()
    object Share : SettingsEffect()
    object SupportUs : SettingsEffect()
    object OnGitHub : SettingsEffect()
    object RemoveAds : SettingsEffect()
    object ThemeDialog : SettingsEffect()
    object Synchronising : SettingsEffect()
    object Synchronised : SettingsEffect()
    object OnlyOneTimeSync : SettingsEffect()
    object AlreadyAdFree : SettingsEffect()
    data class ChangeTheme(val themeValue: Int) : SettingsEffect()
}

// Data
data class SettingsData(var synced: Boolean = false) : BaseData() {
    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }
}

// Extension
fun MutableStateFlow<SettingsState>.update(
    activeCurrencyCount: Int = value.activeCurrencyCount,
    activeWatcherCount: Int = value.activeWatcherCount,
    appThemeType: AppTheme = value.appThemeType,
    addFreeEndDate: String = value.addFreeEndDate,
    loading: Boolean = value.loading
) {
    value = value.copy(
        activeCurrencyCount = activeCurrencyCount,
        activeWatcherCount = activeWatcherCount,
        appThemeType = appThemeType,
        addFreeEndDate = addFreeEndDate,
        loading = loading
    )
}

package com.oztechan.ccc.client.viewmodel.settings

import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.client.viewmodel.settings.model.PremiumStatus

// State
data class SettingsState(
    val activeCurrencyCount: Int = 0,
    val activeWatcherCount: Int = 0,
    val appThemeType: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val premiumStatus: PremiumStatus = PremiumStatus.NeverActivated,
    val precision: Int = 0,
    val version: String = ""
) : BaseState()

// Event
@Suppress("TooManyFunctions")
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onWatchersClick()
    fun onFeedBackClick()
    fun onShareClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
    fun onPremiumClick()
    fun onSyncClick()
    fun onThemeClick()
    fun onPrecisionClick()
    fun onPrecisionSelect(index: Int)
}

// Effect
sealed class SettingsEffect : BaseEffect() {
    object Back : SettingsEffect()
    object OpenCurrencies : SettingsEffect()
    object OpenWatchers : SettingsEffect()
    object FeedBack : SettingsEffect()
    object OnGitHub : SettingsEffect()
    object Premium : SettingsEffect()
    object ThemeDialog : SettingsEffect()
    object Synchronising : SettingsEffect()
    object Synchronised : SettingsEffect()
    object OnlyOneTimeSync : SettingsEffect()
    object AlreadyPremium : SettingsEffect()
    object SelectPrecision : SettingsEffect()
    data class Share(val marketLink: String) : SettingsEffect()
    data class SupportUs(val marketLink: String) : SettingsEffect()
    data class ChangeTheme(val themeValue: Int) : SettingsEffect()
}

// Data
data class SettingsData(var synced: Boolean = false) : BaseData() {
    companion object {
        internal const val SYNC_DELAY = 10.toLong()
    }
}

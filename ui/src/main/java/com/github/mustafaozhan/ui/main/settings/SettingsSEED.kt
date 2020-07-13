/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData

// Event
interface SettingsEvent : BaseEvent {
    fun onBackClick()
    fun onCurrenciesClick()
    fun onFeedBackClick()
    fun onSupportUsClick()
    fun onOnGitHubClick()
}

// Effect
sealed class SettingsEffect : BaseEffect()
object BackEffect : SettingsEffect()
object CurrenciesEffect : SettingsEffect()
object FeedBackEffect : SettingsEffect()
object SupportUsEffect : SettingsEffect()
object OnGitHubEffect : SettingsEffect()

// Data
data class SettingsData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository)

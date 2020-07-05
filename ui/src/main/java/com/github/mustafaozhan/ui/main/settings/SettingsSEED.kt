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
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData

// State
@Suppress("ConstructorParameterNaming")
data class SettingsState(
    private val _state: MutableSettingsState
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = _state._currencyList
    val loading: LiveData<Boolean> = _state._loading
    val selectionVisibility: LiveData<Boolean> = _state._selectionVisibility
}

@Suppress("ConstructorParameterNaming")
data class MutableSettingsState(
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false),
    val _selectionVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
) : MutableBaseState()

// Event
interface SettingsEvent : BaseEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
    fun onItemLongClick(): Boolean
    fun onCloseClick()
}

// Effect
sealed class SettingsEffect : BaseEffect()
object FewCurrencyEffect : SettingsEffect()
object CalculatorEffect : SettingsEffect()
object BackEffect : SettingsEffect()
data class ChangeBaseNavResultEffect(val newBase: String) : SettingsEffect()

// Data
data class SettingsData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {
    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }

    var unFilteredList: MutableList<Currency>? = mutableListOf()
    lateinit var query: String
}

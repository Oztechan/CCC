/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.model.BaseStateBacking
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData

data class SettingsState(
    private val backing: SettingsStateBacking
) : BaseState() {
    val searchQuery: MutableLiveData<String> = backing._searchQuery
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val loading: LiveData<Boolean> = backing._loading
}

@Suppress("ConstructorParameterNaming")
data class SettingsStateBacking(
    val _searchQuery: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>(),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
) : BaseStateBacking()

interface SettingsEvent : BaseEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
}

sealed class SettingsEffect : BaseEffect()
object FewCurrencyEffect : SettingsEffect()
object CalculatorEffect : SettingsEffect()
data class ChangeBaseNavResultEffect(val newBase: String) : SettingsEffect()

data class SettingsData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {
    var unFilteredList: MutableList<Currency>? = mutableListOf()
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.model.MutableBaseState
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData

// State
@Suppress("ConstructorParameterNaming")
data class CalculatorState(
    private val _state: MutableCalculatorState
) : BaseState() {
    val input: LiveData<String> = _state._input
    val base: LiveData<String> = _state._base
    val currencyList: LiveData<MutableList<Currency>> = _state._currencyList
    val output: LiveData<String> = _state._output
    val symbol: LiveData<String> = _state._symbol
    val loading: LiveData<Boolean> = _state._loading
    val dataState: LiveData<DataState> = _state._dataState
}

@Suppress("ConstructorParameterNaming")
data class MutableCalculatorState(
    val _input: MediatorLiveData<String> = MediatorLiveData(),
    val _base: MediatorLiveData<String> = MediatorLiveData(),
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(),
    val _output: MutableLiveData<String> = MutableLiveData(""),
    val _symbol: MutableLiveData<String> = MutableLiveData(""),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(true),
    val _dataState: MutableLiveData<DataState> = MutableLiveData(Error)
) : MutableBaseState()

// Event
interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency, conversion: String)
    fun onItemLongClick(currency: Currency): Boolean
    fun onBarClick()
    fun onSpinnerItemSelected(base: String)
    fun onSettingsClicked()
}

// Effect
sealed class CalculatorEffect : BaseEffect()
object ErrorEffect : CalculatorEffect()
object FewCurrencyEffect : CalculatorEffect()
object OpenBarEffect : CalculatorEffect()
object MaximumInputEffect : CalculatorEffect()
object OpenSettingsEffect : CalculatorEffect()
data class ShowRateEffect(val text: String, val name: String) : CalculatorEffect()

// Data
data class CalculatorData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {

    companion object {
        internal const val MAXIMUM_INPUT = 18
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
        internal const val CHAR_DOT = '.'
    }

    var rates: Rates? = null
}

// Others
sealed class DataState
data class Online(val lastUpdate: String?) : DataState()
data class Cached(val lastUpdate: String?) : DataState()
data class Offline(val lastUpdate: String?) : DataState()
object Error : DataState()

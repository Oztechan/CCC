/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.ccc.android.model.DataState
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.data.model.Currency

// State
@Suppress("ConstructorParameterNaming")
data class CalculatorState(
    private val _state: MutableCalculatorState
) {
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
    val _dataState: MutableLiveData<DataState> = MutableLiveData(DataState.Error)
)

// Event
interface CalculatorEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency, conversion: String)
    fun onItemLongClick(currency: Currency): Boolean
    fun onBarClick()
    fun onSpinnerItemSelected(base: String)
    fun onSettingsClicked()
}

// Effect
sealed class CalculatorEffect
object ErrorEffect : CalculatorEffect()
object FewCurrencyEffect : CalculatorEffect()
object OpenBarEffect : CalculatorEffect()
object MaximumInputEffect : CalculatorEffect()
object OpenSettingsEffect : CalculatorEffect()
data class ShowRateEffect(val text: String, val name: String) : CalculatorEffect()

// Data
data class CalculatorData(var rates: Rates? = null)

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.calculator

import com.github.mustafaozhan.ccc.calculator.Calculator
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.common.model.Rates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State
@Suppress("ConstructorParameterNaming")
data class CalculatorState(
    private val _state: MutableCalculatorState
) {
    val input: StateFlow<String> = _state._input
    val base: StateFlow<String> = _state._base
    val currencyList: StateFlow<List<Currency>> = _state._currencyList
    val output: StateFlow<String> = _state._output
    val symbol: StateFlow<String> = _state._symbol
    val loading: StateFlow<Boolean> = _state._loading
    val dataState: StateFlow<DataState> = _state._dataState
}

@Suppress("ConstructorParameterNaming")
data class MutableCalculatorState(
    val _input: MutableStateFlow<String> = MutableStateFlow(""),
    val _base: MutableStateFlow<String> = MutableStateFlow(""),
    val _currencyList: MutableStateFlow<List<Currency>> = MutableStateFlow(mutableListOf()),
    val _output: MutableStateFlow<String> = MutableStateFlow(""),
    val _symbol: MutableStateFlow<String> = MutableStateFlow(""),
    val _loading: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val _dataState: MutableStateFlow<DataState> = MutableStateFlow(DataState.Error)
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
data class CalculatorData(
    var calculator: Calculator = Calculator(),
    var rates: Rates? = null
)

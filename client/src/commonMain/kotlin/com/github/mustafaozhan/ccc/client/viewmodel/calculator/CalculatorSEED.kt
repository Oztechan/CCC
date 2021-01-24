/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.calculator

import com.github.mustafaozhan.ccc.calculator.Calculator
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.common.model.Rates
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class CalculatorState(
    val input: String = "",
    val base: String = "",
    val currencyList: List<Currency> = listOf(),
    val output: String = "",
    val symbol: String = "",
    val loading: Boolean = true,
    val dataState: DataState = DataState.Error,
) {
    companion object {
        @Suppress("LongParameterList")
        fun MutableStateFlow<CalculatorState>.update(
            input: String = value.input,
            base: String = value.base,
            currencyList: List<Currency> = value.currencyList,
            output: String = value.output,
            symbol: String = value.symbol,
            loading: Boolean = value.loading,
            dataState: DataState = value.dataState
        ) {
            value = value.copy(
                input = input,
                base = base,
                currencyList = currencyList,
                output = output,
                symbol = symbol,
                loading = loading,
                dataState = dataState
            )
        }
    }
}

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
sealed class CalculatorEffect {
    object Error : CalculatorEffect()
    object FewCurrency : CalculatorEffect()
    object OpenBar : CalculatorEffect()
    object MaximumInput : CalculatorEffect()
    object OpenSettings : CalculatorEffect()
    data class ShowRate(val text: String, val name: String) : CalculatorEffect()
}

// Data
data class CalculatorData(
    var calculator: Calculator = Calculator(),
    var rates: Rates? = null
)

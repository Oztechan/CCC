/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.currencies

import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State
@Suppress("ConstructorParameterNaming")
data class CurrenciesState(
    private val _state: MutableCurrenciesState
) {
    val currencyList: StateFlow<MutableList<Currency>> = _state._currencyList
    val loading: StateFlow<Boolean> = _state._loading
    val selectionVisibility: StateFlow<Boolean> = _state._selectionVisibility
}

@Suppress("ConstructorParameterNaming")
data class MutableCurrenciesState(
    val _currencyList: MutableStateFlow<MutableList<Currency>> = MutableStateFlow(mutableListOf()),
    val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _selectionVisibility: MutableStateFlow<Boolean> = MutableStateFlow(false)
)

// Event
interface CurrenciesEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
    fun onItemLongClick(): Boolean
    fun onCloseClick()
}

// Effect
sealed class CurrenciesEffect
object FewCurrencyEffect : CurrenciesEffect()
object CalculatorEffect : CurrenciesEffect()
object BackEffect : CurrenciesEffect()
data class ChangeBaseNavResultEffect(val newBase: String) : CurrenciesEffect()

// Data
data class CurrenciesData(
    var unFilteredList: MutableList<Currency>? = mutableListOf(),
    var query: String = ""
)

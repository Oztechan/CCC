/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.bar

import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State
@Suppress("ConstructorParameterNaming")
data class BarState(
    private val _state: MutableBarState
) {
    val currencyList: StateFlow<List<Currency>> = _state._currencyList
    val loading: StateFlow<Boolean> = _state._loading
    val enoughCurrency: StateFlow<Boolean> = _state._enoughCurrency
}

@Suppress("ConstructorParameterNaming")
data class MutableBarState(
    val _currencyList: MutableStateFlow<List<Currency>> = MutableStateFlow(mutableListOf()),
    val _loading: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val _enoughCurrency: MutableStateFlow<Boolean> = MutableStateFlow(false)
)

// Event
interface BarEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class BarEffect
data class ChangeBaseNavResultEffect(val newBase: String) : BarEffect()
object OpenCurrenciesEffect : BarEffect()

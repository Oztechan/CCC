/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.bar

import com.github.mustafaozhan.ccc.client.model.Currency

// State
data class BarState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false
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

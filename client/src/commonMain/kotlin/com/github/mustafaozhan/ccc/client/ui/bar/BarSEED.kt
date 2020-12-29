/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.bar

import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class BarState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false
) {
    companion object {
        fun MutableStateFlow<BarState>.update(
            currencyList: List<Currency> = value.currencyList,
            loading: Boolean = value.loading,
            enoughCurrency: Boolean = value.enoughCurrency
        ) {
            value = value.copy(
                currencyList = currencyList,
                loading = loading,
                enoughCurrency = enoughCurrency
            )
        }
    }
}

// Event
interface BarEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class BarEffect
data class ChangeBaseNavResultEffect(val newBase: String) : BarEffect()
object OpenCurrenciesEffect : BarEffect()

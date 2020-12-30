/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.currencies

import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class CurrenciesState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = false,
    val selectionVisibility: Boolean = false
) {
    companion object {
        fun MutableStateFlow<CurrenciesState>.update(
            currencyList: List<Currency> = value.currencyList,
            loading: Boolean = value.loading,
            selectionVisibility: Boolean = value.selectionVisibility
        ) {
            value = value.copy(
                currencyList = currencyList,
                loading = loading,
                selectionVisibility = selectionVisibility
            )
        }
    }
}

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

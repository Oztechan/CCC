package com.github.mustafaozhan.ccc.client.viewmodel.currencies

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class CurrenciesState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = true,
    val selectionVisibility: Boolean = false
) : BaseState()

// Event
interface CurrenciesEvent : BaseEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
    fun onItemLongClick()
    fun onCloseClick()
    fun onQueryChange(query: String)
}

// Effect
sealed class CurrenciesEffect : BaseEffect() {
    object FewCurrency : CurrenciesEffect()
    object OpenCalculator : CurrenciesEffect()
    object Back : CurrenciesEffect()
    data class ChangeBase(val newBase: String) : CurrenciesEffect()
}

// Data
data class CurrenciesData(
    var unFilteredList: MutableList<Currency> = mutableListOf(),
    var query: String = ""
) : BaseData()

// Extension
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

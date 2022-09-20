package com.oztechan.ccc.client.viewmodel.currencies

import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.Currency
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
) : BaseData() {
    companion object {
        internal const val MINIMUM_ACTIVE_CURRENCY = 2
    }
}

// Extension
internal fun MutableStateFlow<CurrenciesState>.update(
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

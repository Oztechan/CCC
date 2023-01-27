package com.oztechan.ccc.client.viewmodel.currencies

import com.oztechan.ccc.client.viewmodel.BaseData
import com.oztechan.ccc.client.viewmodel.BaseEffect
import com.oztechan.ccc.client.viewmodel.BaseEvent
import com.oztechan.ccc.client.viewmodel.BaseState
import com.oztechan.ccc.common.core.model.Currency

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

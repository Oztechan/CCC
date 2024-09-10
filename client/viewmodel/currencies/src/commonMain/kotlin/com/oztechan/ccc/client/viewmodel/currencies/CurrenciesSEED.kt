package com.oztechan.ccc.client.viewmodel.currencies

import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.common.core.model.Currency

// State
data class CurrenciesState(
    val isBannerAdVisible: Boolean,
    val isOnboardingVisible: Boolean,
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = true,
    val selectionVisibility: Boolean = false
) : BaseState

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
sealed class CurrenciesEffect : BaseEffect {
    data object FewCurrency : CurrenciesEffect()
    data object OpenCalculator : CurrenciesEffect()
    data object Back : CurrenciesEffect()
}

// Data
data class CurrenciesData(
    var unFilteredList: MutableList<Currency> = mutableListOf(),
    var query: String = ""
) : BaseData

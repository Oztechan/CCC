package com.oztechan.ccc.client.viewmodel.selectcurrency

import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.common.core.model.Currency

// State
data class SelectCurrencyState(
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false,
    val currencyList: List<Currency> = listOf(),
) : BaseState

// Event
interface SelectCurrencyEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class SelectCurrencyEffect : BaseEffect {
    data object CurrencyChange : SelectCurrencyEffect()
    data object OpenCurrencies : SelectCurrencyEffect()
}

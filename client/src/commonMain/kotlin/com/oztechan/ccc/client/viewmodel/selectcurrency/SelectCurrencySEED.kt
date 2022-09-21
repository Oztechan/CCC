package com.oztechan.ccc.client.viewmodel.selectcurrency

import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class SelectCurrencyState(
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false,
    val currencyList: List<Currency> = listOf(),
) : BaseState()

// Event
interface SelectCurrencyEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class SelectCurrencyEffect : BaseEffect() {
    data class CurrencyChange(val newBase: String) : SelectCurrencyEffect()
    object OpenCurrencies : SelectCurrencyEffect()
}

// Extension
internal fun MutableStateFlow<SelectCurrencyState>.update(
    loading: Boolean = value.loading,
    enoughCurrency: Boolean = value.enoughCurrency,
    currencyList: List<Currency> = value.currencyList
) {
    value = value.copy(
        loading = loading,
        enoughCurrency = enoughCurrency,
        currencyList = currencyList
    )
}

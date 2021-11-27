package com.github.mustafaozhan.ccc.client.viewmodel.changebase

import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class ChangeBaseState(
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false,
    val currencyList: List<Currency> = listOf(),
) : BaseState()

// Event
interface ChangeBaseEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class ChangeBaseEffect : BaseEffect() {
    data class BaseChange(val newBase: String) : ChangeBaseEffect()
    object OpenCurrencies : ChangeBaseEffect()
}

// Extension
fun MutableStateFlow<ChangeBaseState>.update(
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

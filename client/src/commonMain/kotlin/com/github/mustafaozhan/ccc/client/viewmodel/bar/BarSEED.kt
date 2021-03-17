package com.github.mustafaozhan.ccc.client.viewmodel.bar

import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class BarState(
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false,
    val currencyList: List<Currency> = listOf(),
) : BaseState() {
    // for ios
    constructor() : this(true, false, listOf())

    companion object {
        fun MutableStateFlow<BarState>.update(
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
    }
}

// Event
interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class BarEffect : BaseEffect() {
    data class ChangeBase(val newBase: String) : BarEffect()
    object OpenCurrencies : BarEffect()
}

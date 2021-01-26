/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.bar

import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class BarState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false
) : BaseState() {
    constructor() : this(listOf(), true, false)

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
interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

// Effect
sealed class BarEffect : BaseEffect() {
    data class ChangeBase(val newBase: String) : BarEffect()
    object OpenCurrencies : BarEffect()
}

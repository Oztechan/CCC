/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BarViewModel(private val currencyDao: CurrencyDao) : BaseSEEDViewModel(), BarEvent {
    // region SEED
    private val _state = MutableStateFlow(BarState())
    override val state: StateFlow<BarState> = _state

    private val _effect = Channel<BarEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as BarEvent

    override val data: BaseData? = null
    // endregion

    init {
        kermit.d { "BarViewModel init" }

        clientScope.launch {
            currencyDao.collectActiveCurrencies()
                .mapToModel()
                .collect {
                    _state.update(
                        currencyList = it,
                        loading = false,
                        enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                    )
                }
        }
    }

    override fun onCleared() {
        kermit.d { "BarViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onItemClick(currency: Currency) = clientScope.launch {
        kermit.d { "BarViewModel onItemClick ${currency.name}" }
        _effect.send(BarEffect.ChangeBase(currency.name))
    }.toUnit()

    override fun onSelectClick() = clientScope.launch {
        kermit.d { "BarViewModel onSelectClick" }
        _effect.send(BarEffect.OpenCurrencies)
    }.toUnit()
    // endregion
}

// region SEED
data class BarState(
    val loading: Boolean = true,
    val enoughCurrency: Boolean = false,
    val currencyList: List<Currency> = listOf(),
) : BaseState() {
    // for ios
    constructor() : this(true, false, listOf())
}

interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

sealed class BarEffect : BaseEffect() {
    data class ChangeBase(val newBase: String) : BarEffect()
    object OpenCurrencies : BarEffect()
}
// endregion

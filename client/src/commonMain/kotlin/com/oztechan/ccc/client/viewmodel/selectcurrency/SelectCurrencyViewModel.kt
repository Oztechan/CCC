/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.update
import com.oztechan.ccc.client.viewmodel.BaseData
import com.oztechan.ccc.client.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelectCurrencyViewModel(
    currencyDataSource: CurrencyDataSource
) : BaseSEEDViewModel<SelectCurrencyState, SelectCurrencyEffect, SelectCurrencyEvent, BaseData>(), SelectCurrencyEvent {
    // region SEED
    private val _state = MutableStateFlow(SelectCurrencyState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SelectCurrencyEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as SelectCurrencyEvent

    override val data: BaseData? = null
    // endregion

    init {
        currencyDataSource.getActiveCurrenciesFlow()
            .onEach {
                _state.update {
                    copy(
                        currencyList = it,
                        loading = false,
                        enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                    )
                }
            }.launchIn(viewModelScope)
    }

    // region Event
    override fun onItemClick(currency: Currency) = viewModelScope.launchIgnored {
        Logger.d { "SelectCurrencyViewModel onItemClick ${currency.code}" }
        _effect.emit(SelectCurrencyEffect.CurrencyChange(currency.code))
    }

    override fun onSelectClick() = viewModelScope.launchIgnored {
        Logger.d { "SelectCurrencyViewModel onSelectClick" }
        _effect.emit(SelectCurrencyEffect.OpenCurrencies)
    }
    // endregion
}

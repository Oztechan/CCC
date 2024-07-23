/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.constants.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelectCurrencyViewModel(
    currencyDataSource: CurrencyDataSource
) : SEEDViewModel<SelectCurrencyState, SelectCurrencyEffect, SelectCurrencyEvent, BaseData>(
    initialState = SelectCurrencyState()
),
    SelectCurrencyEvent {
    // region SEED
    private val _effect = MutableSharedFlow<SelectCurrencyEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as SelectCurrencyEvent
    // endregion

    init {
        currencyDataSource.getActiveCurrenciesFlow()
            .onEach {
                setState {
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

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelectCurrencyViewModel(
    currencyDataSource: CurrencyDataSource
) : BaseSEEDViewModel(), SelectCurrencyEvent {
    // region SEED
    private val _state = MutableStateFlow(SelectCurrencyState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SelectCurrencyEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as SelectCurrencyEvent

    override val data: BaseData? = null
    // endregion

    init {
        currencyDataSource.collectActiveCurrencies()
            .onEach {
                _state.update(
                    currencyList = it.toUIModelList(),
                    loading = false,
                    enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                )
            }.launchIn(viewModelScope)
    }

    // region Event
    override fun onItemClick(currency: Currency) = viewModelScope.launchIgnored {
        Logger.d { "SelectCurrencyViewModel onItemClick ${currency.name}" }
        _effect.emit(SelectCurrencyEffect.CurrencyChange(currency.name))
    }

    override fun onSelectClick() = viewModelScope.launchIgnored {
        Logger.d { "SelectCurrencyViewModel onSelectClick" }
        _effect.emit(SelectCurrencyEffect.OpenCurrencies)
    }
    // endregion
}

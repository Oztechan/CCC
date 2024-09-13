/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.constants.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.viewmodel.selectcurrency.model.SelectCurrencyPurpose
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SelectCurrencyViewModel(
    private val calculationStorage: CalculationStorage,
    currencyDataSource: CurrencyDataSource,
    private val watcherDataSource: WatcherDataSource
) : SEEDViewModel<SelectCurrencyState, SelectCurrencyEffect, SelectCurrencyEvent, BaseData>(
    initialState = SelectCurrencyState()
),
    SelectCurrencyEvent {

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
    override fun onItemClick(currency: Currency, purpose: SelectCurrencyPurpose) {
        Logger.d { "SelectCurrencyViewModel onItemClick ${currency.code} $purpose" }
        viewModelScope.launch {
            when (purpose) {
                SelectCurrencyPurpose.Base -> calculationStorage.currentBase = currency.code
                is SelectCurrencyPurpose.Source -> watcherDataSource.updateWatcherBaseById(
                    currency.code,
                    purpose.watcher.id
                )

                is SelectCurrencyPurpose.Target -> watcherDataSource.updateWatcherTargetById(
                    currency.code,
                    purpose.watcher.id
                )
            }
            sendEffect { SelectCurrencyEffect.CurrencySelected }
        }
    }

    override fun onSelectClick() {
        Logger.d { "SelectCurrencyViewModel onSelectClick" }
        sendEffect { SelectCurrencyEffect.OpenCurrencies }
    }
    // endregion
}

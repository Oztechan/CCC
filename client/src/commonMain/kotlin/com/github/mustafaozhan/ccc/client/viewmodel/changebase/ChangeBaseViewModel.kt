/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.changebase

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.mapper.toUIModelList
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChangeBaseViewModel(
    currencyRepository: CurrencyRepository
) : BaseSEEDViewModel(), ChangeBaseEvent {
    // region SEED
    private val _state = MutableStateFlow(ChangeBaseState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ChangeBaseEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as ChangeBaseEvent

    override val data: BaseData? = null
    // endregion

    init {
        currencyRepository.collectActiveCurrencies()
            .onEach {
                _state.update(
                    currencyList = it.toUIModelList(),
                    loading = false,
                    enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                )
            }.launchIn(clientScope)
    }

    // region Event
    override fun onItemClick(currency: Currency) = clientScope.launchIgnored {
        Logger.d { "ChangeBaseViewModel onItemClick ${currency.name}" }
        _effect.emit(ChangeBaseEffect.BaseChange(currency.name))
    }

    override fun onSelectClick() = clientScope.launchIgnored {
        Logger.d { "ChangeBaseViewModel onSelectClick" }
        _effect.emit(ChangeBaseEffect.OpenCurrencies)
    }
    // endregion
}

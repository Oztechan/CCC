/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.bar

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.mapper.toUIModelList
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BarViewModel(
    currencyRepository: CurrencyRepository
) : BaseSEEDViewModel(), BarEvent {
    // region SEED
    private val _state = MutableStateFlow(BarState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BarEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as BarEvent

    override val data: BaseData? = null
    // endregion

    init {
        kermit.d { "BarViewModel init" }

        currencyRepository.collectActiveCurrencies()
            .onEach {
                _state.update(
                    currencyList = it.toUIModelList(),
                    loading = false,
                    enoughCurrency = it.size >= MINIMUM_ACTIVE_CURRENCY
                )
            }.launchIn(clientScope)
    }

    override fun onCleared() {
        kermit.d { "BarViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onItemClick(currency: Currency) = clientScope.launchIgnored {
        kermit.d { "BarViewModel onItemClick ${currency.name}" }
        _effect.emit(BarEffect.ChangeBase(currency.name))
    }

    override fun onSelectClick() = clientScope.launchIgnored {
        kermit.d { "BarViewModel onSelectClick" }
        _effect.emit(BarEffect.OpenCurrencies)
    }
    // endregion
}

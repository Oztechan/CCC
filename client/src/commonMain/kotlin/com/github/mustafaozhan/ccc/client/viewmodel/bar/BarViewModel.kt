/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.bar

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarState.Companion.update
import com.github.mustafaozhan.ccc.common.data.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BarViewModel(
    private val currencyDao: CurrencyDao,
    private val settingsRepository: SettingsRepository
) : BaseViewModel(), BarEvent {
    // region SEED
    private val _state = MutableStateFlow(BarState())
    val state: StateFlow<BarState> = _state

    private val _effect = Channel<BarEffect>(1)
    val effect = _effect.receiveAsFlow().conflate()

    val event = this as BarEvent
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
        settingsRepository.currentBase = currency.name
        _effect.send(BarEffect.ChangeBase(currency.name))
    }.toUnit()

    override fun onSelectClick() = clientScope.launch {
        kermit.d { "BarViewModel onSelectClick" }
        _effect.send(BarEffect.OpenCurrencies)
    }.toUnit()
    // endregion
}

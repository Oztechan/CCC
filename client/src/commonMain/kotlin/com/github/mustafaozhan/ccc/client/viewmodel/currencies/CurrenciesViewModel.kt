/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.currencies

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesState.Companion.update
import com.github.mustafaozhan.ccc.common.data.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CurrenciesViewModel(
    private val settingsRepository: SettingsRepository,
    private val currencyDao: CurrencyDao
) : BaseViewModel(), CurrenciesEvent {

    // region SEED
    private val _state = MutableStateFlow(CurrenciesState())
    val state: StateFlow<CurrenciesState> = _state

    private val _effect = Channel<CurrenciesEffect>(1)
    val effect = _effect.receiveAsFlow().conflate()

    val data = CurrenciesData()

    fun getEvent() = this as CurrenciesEvent
    // endregion

    init {
        kermit.d { "CurrenciesViewModel init" }
        _state.update(loading = true)

        clientScope.launch {
            currencyDao.collectAllCurrencies()
                .mapToModel()
                .collect { currencyList ->

                    _state.update(currencyList = currencyList)
                    data.unFilteredList = currencyList.toMutableList()

                    currencyList
                        .filter { it.isActive }.size
                        .whether { it < MINIMUM_ACTIVE_CURRENCY }
                        ?.whetherNot { settingsRepository.firstRun }
                        ?.let { _effect.send(FewCurrencyEffect) }

                    verifyCurrentBase()
                    filterList(data.query)
                    _state.update(selectionVisibility = false)
                }
        }
        filterList("")
    }

    private fun verifyCurrentBase() = settingsRepository.currentBase.either(
        { isEmpty() },
        { base ->
            state.value.currencyList
                .filter { it.name == base }
                .toList().firstOrNull()?.isActive == false
        }
    )?.let {
        updateCurrentBase(
            state.value.currencyList
                .firstOrNull { it.isActive }?.name ?: ""
        )
    }

    private fun updateCurrentBase(newBase: String) = clientScope.launch {
        settingsRepository.currentBase = newBase
        _effect.send(ChangeBaseNavResultEffect(newBase))
    }.toUnit()

    fun hideSelectionVisibility() {
        _state.update(selectionVisibility = false)
    }

    fun filterList(txt: String) = data.unFilteredList
        ?.filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                    longName.contains(txt, true) ||
                    symbol.contains(txt, true)
        }?.toMutableList()
        ?.let {
            _state.update(
                currencyList = it,
                loading = false
            )
        }.run {
            data.query = txt
            true
        }

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun isFirstRun() = settingsRepository.firstRun

    override fun onCleared() {
        kermit.d { "CurrenciesViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) {
        kermit.d { "CurrenciesViewModel updateAllCurrenciesState $state" }
        currencyDao.updateAllCurrencyState(state)
    }

    override fun onItemClick(currency: Currency) {
        kermit.d { "CurrenciesViewModel onItemClick ${currency.name}" }
        currencyDao.updateCurrencyStateByName(currency.name, !currency.isActive)
    }

    override fun onDoneClick() = clientScope.launch {
        kermit.d { "CurrenciesViewModel onDoneClick" }
        _state.value.currencyList
            .filter { it.isActive }.size
            .whether { it < MINIMUM_ACTIVE_CURRENCY }
            ?.let { _effect.send(FewCurrencyEffect) }
            ?: run {
                settingsRepository.firstRun = false
                _effect.send(CalculatorEffect)
            }
    }.toUnit()

    override fun onItemLongClick() = _state.value.selectionVisibility.let {
        kermit.d { "CurrenciesViewModel onItemLongClick" }
        _state.update(selectionVisibility = !it)
        true
    }

    override fun onCloseClick() = clientScope.launch {
        kermit.d { "CurrenciesViewModel onCloseClick" }
        if (_state.value.selectionVisibility) {
            _state.update(selectionVisibility = false)
        } else {
            _effect.send(BackEffect)
        }.run {
            filterList("")
        }
    }.toUnit()
    // endregion
}

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
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
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
) : BaseSEEDViewModel(), CurrenciesEvent {

    // region SEED
    private val _state = MutableStateFlow(CurrenciesState())
    override val state: StateFlow<CurrenciesState> = _state

    private val _effect = Channel<CurrenciesEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as CurrenciesEvent

    override val data = CurrenciesData()
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
                        ?.let { _effect.send(CurrenciesEffect.FewCurrency) }

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
        (state.value.currencyList.firstOrNull { it.isActive }?.name ?: "").let { newBase ->
            settingsRepository.currentBase = newBase
            clientScope.launch { _effect.send(CurrenciesEffect.ChangeBase(newBase)) }
        }
    }

    fun hideSelectionVisibility() {
        _state.update(selectionVisibility = false)
    }

    fun filterList(txt: String) = data.unFilteredList
        .filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                    longName.contains(txt, true) ||
                    symbol.contains(txt, true)
        }.toMutableList()
        .let {
            _state.update(currencyList = it, loading = false)
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
        data.unFilteredList
            .filter { it.isActive }.size
            .whether { it < MINIMUM_ACTIVE_CURRENCY }
            ?.let { _effect.send(CurrenciesEffect.FewCurrency) }
            ?: run {
                settingsRepository.firstRun = false
                _effect.send(CurrenciesEffect.OpenCalculator)
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
            _effect.send(CurrenciesEffect.Back)
        }.run {
            filterList("")
        }
    }.toUnit()
    // endregion
}

// region SEED
data class CurrenciesState(
    val currencyList: List<Currency> = listOf(),
    val loading: Boolean = false,
    val selectionVisibility: Boolean = false
) : BaseState() {
    // for ios
    constructor() : this(listOf(), false, false)
}

interface CurrenciesEvent : BaseEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
    fun onItemLongClick(): Boolean
    fun onCloseClick()
}

sealed class CurrenciesEffect : BaseEffect() {
    object FewCurrency : CurrenciesEffect()
    object OpenCalculator : CurrenciesEffect()
    object Back : CurrenciesEffect()
    data class ChangeBase(val newBase: String) : CurrenciesEffect()
}

data class CurrenciesData(
    var unFilteredList: MutableList<Currency> = mutableListOf(),
    var query: String = ""
) : BaseData()
// endregion

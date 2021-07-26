/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.currencies

import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.mapper.toUIModelList
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.isEmptyOrNullString
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CurrenciesViewModel(
    private val settingsRepository: SettingsRepository,
    private val currencyRepository: CurrencyRepository
) : BaseSEEDViewModel(), CurrenciesEvent {
    // region SEED
    private val _state = MutableStateFlow(CurrenciesState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CurrenciesEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as CurrenciesEvent

    override val data = CurrenciesData()
    // endregion

    init {
        kermit.d { "CurrenciesViewModel init" }

        currencyRepository.collectAllCurrencies()
            .map { it.toUIModelList() }
            .onEach { currencyList ->

                _state.update(currencyList = currencyList)
                data.unFilteredList = currencyList.toMutableList()

                verifyListSize()
                verifyCurrentBase()

                filterList(data.query)
                _state.update(selectionVisibility = false)
            }.launchIn(clientScope)

        filterList("")
    }

    private fun verifyListSize() = _state.value.currencyList
        .filter { it.isActive }.size
        .whether { it < MINIMUM_ACTIVE_CURRENCY }
        ?.whetherNot { settingsRepository.firstRun }
        ?.mapTo { clientScope }
        ?.launch { _effect.emit(CurrenciesEffect.FewCurrency) }

    private fun verifyCurrentBase() = settingsRepository.currentBase.either(
        { isEmptyOrNullString() },
        { base ->
            state.value.currencyList
                .filter { it.name == base }
                .toList().firstOrNull()?.isActive == false
        }
    )?.let {
        (state.value.currencyList.firstOrNull { it.isActive }?.name ?: "").let { newBase ->
            settingsRepository.currentBase = newBase
            clientScope.launch { _effect.emit(CurrenciesEffect.ChangeBase(newBase)) }
        }
    }

    private fun filterList(txt: String) = data.unFilteredList
        .filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }.toMutableList()
        .let {
            _state.update(currencyList = it, loading = false)
        }.run {
            data.query = txt
        }

    fun hideSelectionVisibility() {
        _state.update(selectionVisibility = false)
    }

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    fun isFirstRun() = settingsRepository.firstRun

    override fun onCleared() {
        kermit.d { "CurrenciesViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) {
        kermit.d { "CurrenciesViewModel updateAllCurrenciesState $state" }
        currencyRepository.updateAllCurrencyState(state)
    }

    override fun onItemClick(currency: Currency) {
        kermit.d { "CurrenciesViewModel onItemClick ${currency.name}" }
        currencyRepository.updateCurrencyStateByName(currency.name, !currency.isActive)
    }

    override fun onDoneClick() = clientScope.launchIgnored {
        kermit.d { "CurrenciesViewModel onDoneClick" }
        data.unFilteredList
            .filter { it.isActive }.size
            .whether { it < MINIMUM_ACTIVE_CURRENCY }
            ?.let { _effect.emit(CurrenciesEffect.FewCurrency) }
            ?: run {
                settingsRepository.firstRun = false
                _effect.emit(CurrenciesEffect.OpenCalculator)
            }
    }

    override fun onItemLongClick() = _state.value.selectionVisibility.let {
        kermit.d { "CurrenciesViewModel onItemLongClick" }
        _state.update(selectionVisibility = !it)
    }

    override fun onCloseClick() = clientScope.launchIgnored {
        kermit.d { "CurrenciesViewModel onCloseClick" }
        if (_state.value.selectionVisibility) {
            _state.update(selectionVisibility = false)
        } else {
            _effect.emit(CurrenciesEffect.Back)
        }.run {
            filterList("")
        }
    }

    override fun onQueryChange(query: String) {
        kermit.d { "CurrenciesViewModel onQueryChange $query" }
        filterList(query)
    }
    // endregion
}

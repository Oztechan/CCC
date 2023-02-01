/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.currencies

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.either
import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.constants.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.client.core.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.core.viewmodel.util.update
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions")
class CurrenciesViewModel(
    private val appStorage: AppStorage,
    private val calculatorStorage: CalculatorStorage,
    private val currencyDataSource: CurrencyDataSource,
    private val adControlRepository: AdControlRepository,
    private val analyticsManager: AnalyticsManager
) : BaseSEEDViewModel<CurrenciesState, CurrenciesEffect, CurrenciesEvent, CurrenciesData>(), CurrenciesEvent {
    // region SEED
    private val _state = MutableStateFlow(CurrenciesState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CurrenciesEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as CurrenciesEvent

    override val data = CurrenciesData()
    // endregion

    init {
        currencyDataSource.getCurrenciesFlow()
            .onEach { currencyList ->

                _state.update {
                    copy(
                        currencyList = currencyList,
                        selectionVisibility = false
                    )
                }
                data.unFilteredList = currencyList.toMutableList()

                verifyListSize()
                verifyCurrentBase()

                filterList(data.query)

                currencyList.filter { it.isActive }
                    .let {
                        analyticsManager.setUserProperty(UserProperty.CurrencyCount(it.count().toString()))
                    }
            }.launchIn(viewModelScope)

        filterList("")
    }

    private suspend fun verifyListSize() = _state.value.currencyList
        .filter { it.isActive }
        .whether { it.size < MINIMUM_ACTIVE_CURRENCY }
        ?.whetherNot { appStorage.firstRun }
        ?.run { _effect.emit(CurrenciesEffect.FewCurrency) }

    private suspend fun verifyCurrentBase() = calculatorStorage.currentBase.either(
        { isEmpty() },
        { base ->
            state.value.currencyList
                .filter { it.code == base }
                .toList().firstOrNull()?.isActive == false
        }
    )?.mapTo {
        state.value.currencyList.firstOrNull { it.isActive }?.code.orEmpty()
    }?.let { newBase ->
        calculatorStorage.currentBase = newBase

        analyticsManager.trackEvent(Event.BaseChange(Param.Base(newBase)))
        analyticsManager.setUserProperty(UserProperty.BaseCurrency(newBase))

        _effect.emit(CurrenciesEffect.ChangeBase(newBase))
    }

    private fun filterList(txt: String) = data.unFilteredList
        .filter { (code, name, symbol) ->
            code.contains(txt, true) ||
                name.contains(txt, true) ||
                symbol.contains(txt, true)
        }.toMutableList()
        .let {
            _state.update { copy(currencyList = it, loading = false) }
        }.run {
            data.query = txt
        }

    fun hideSelectionVisibility() = _state.update {
        copy(selectionVisibility = false)
    }

    fun shouldShowBannerAd() = adControlRepository.shouldShowBannerAd()

    fun isFirstRun() = appStorage.firstRun

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel updateAllCurrenciesState $state" }
        currencyDataSource.updateCurrencyStates(state)
    }

    override fun onItemClick(currency: Currency) = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onItemClick ${currency.code}" }
        currencyDataSource.updateCurrencyStateByCode(currency.code, !currency.isActive)
    }

    override fun onDoneClick() = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onDoneClick" }
        data.unFilteredList
            .filter { it.isActive }.size
            .whether { it < MINIMUM_ACTIVE_CURRENCY }
            ?.let { _effect.emit(CurrenciesEffect.FewCurrency) }
            ?: run {
                appStorage.firstRun = false
                filterList("")
                _effect.emit(CurrenciesEffect.OpenCalculator)
            }
    }

    override fun onItemLongClick() = _state.value.selectionVisibility.let {
        Logger.d { "CurrenciesViewModel onItemLongClick" }
        _state.update { copy(selectionVisibility = !it) }
    }

    override fun onCloseClick() = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onCloseClick" }
        if (_state.value.selectionVisibility) {
            _state.update { copy(selectionVisibility = false) }
        } else {
            _effect.emit(CurrenciesEffect.Back)
        }.run {
            filterList("")
        }
    }

    override fun onQueryChange(query: String) {
        Logger.d { "CurrenciesViewModel onQueryChange $query" }
        filterList(query)
    }
    // endregion
}

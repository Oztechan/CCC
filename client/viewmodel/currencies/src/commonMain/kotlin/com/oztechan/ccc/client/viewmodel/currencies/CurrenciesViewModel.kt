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
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("TooManyFunctions")
class CurrenciesViewModel(
    private val appStorage: AppStorage,
    private val calculationStorage: CalculationStorage,
    private val currencyDataSource: CurrencyDataSource,
    adControlRepository: AdControlRepository,
    private val analyticsManager: AnalyticsManager
) : SEEDViewModel<CurrenciesState, CurrenciesEffect, CurrenciesEvent, CurrenciesData>(
    initialState = CurrenciesState(
        isBannerAdVisible = adControlRepository.shouldShowBannerAd(),
        isOnboardingVisible = appStorage.firstRun
    ),
    initialData = CurrenciesData()
),
    CurrenciesEvent {
    // region SEED
    override val event = this as CurrenciesEvent
    // endregion

    init {
        currencyDataSource.getCurrenciesFlow()
            .onEach { currencyList ->

                data.unFilteredList = currencyList.toMutableList()
                filterList(data.query)

                verifyListSize()
                verifyCurrentBase()

                currencyList.filter { it.isActive }
                    .let {
                        analyticsManager.setUserProperty(
                            UserProperty.CurrencyCount(
                                it.count().toString()
                            )
                        )
                    }
            }.launchIn(viewModelScope)

        filterList("")
    }

    private suspend fun verifyListSize() = data.unFilteredList
        .filter { it.isActive }
        .whether { it.size < MINIMUM_ACTIVE_CURRENCY }
        ?.whetherNot { appStorage.firstRun }
        ?.run { setEffect { CurrenciesEffect.FewCurrency } }

    private suspend fun verifyCurrentBase() = calculationStorage.currentBase.either(
        { it.isEmpty() },
        { base ->
            state.value.currencyList
                .filter { it.code == base }
                .toList().firstOrNull()?.isActive == false
        }
    )?.mapTo {
        state.value.currencyList.firstOrNull { it.isActive }?.code.orEmpty()
    }?.let { newBase ->
        calculationStorage.currentBase = newBase

        analyticsManager.trackEvent(Event.BaseChange(Param.Base(newBase)))
        analyticsManager.setUserProperty(UserProperty.BaseCurrency(newBase))

        setEffect { CurrenciesEffect.ChangeBase(newBase) }
    }

    private fun filterList(txt: String) = data.unFilteredList
        .filter { (code, name, symbol) ->
            code.contains(txt, true) ||
                name.contains(txt, true) ||
                symbol.contains(txt, true)
        }.toMutableList()
        .let {
            setState { copy(currencyList = it, loading = false) }
        }.run {
            data.query = txt
        }

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
            ?.let { setEffect { CurrenciesEffect.FewCurrency } }
            ?: run {
                appStorage.firstRun = false
                setState { copy(isOnboardingVisible = false) }
                filterList("")
                setEffect { CurrenciesEffect.OpenCalculator }
            }
    }

    override fun onItemLongClick() = state.value.selectionVisibility.let {
        Logger.d { "CurrenciesViewModel onItemLongClick" }
        setState { copy(selectionVisibility = !it) }
    }

    override fun onCloseClick() = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onCloseClick" }
        if (state.value.selectionVisibility) {
            setState { copy(selectionVisibility = false) }
        } else {
            setEffect { CurrenciesEffect.Back }
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

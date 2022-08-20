/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.currencies

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.either
import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.analytics.model.Param
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
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
    private val settingsDataSource: SettingsDataSource,
    private val currencyDataSource: CurrencyDataSource,
    private val adRepository: AdRepository,
    private val analyticsManager: AnalyticsManager
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
        currencyDataSource.collectAllCurrencies()
            .map { it.toUIModelList() }
            .onEach { currencyList ->

                _state.update(
                    currencyList = currencyList,
                    selectionVisibility = false
                )
                data.unFilteredList = currencyList.toMutableList()

                verifyListSize()
                verifyCurrentBase()

                filterList(data.query)

                currencyList.filter { it.isActive }
                    .run {
                        analyticsManager.setUserProperty(UserProperty.CurrencyCount(currencyList.count().toString()))
                        analyticsManager.setUserProperty(
                            UserProperty.ActiveCurrencies(currencyList.joinToString(",") { currency -> currency.name })
                        )
                    }
            }.launchIn(viewModelScope)

        filterList("")
    }

    private fun verifyListSize() = _state.value.currencyList
        .filter { it.isActive }.size
        .whether { it < MINIMUM_ACTIVE_CURRENCY }
        ?.whetherNot { settingsDataSource.firstRun }
        ?.mapTo { viewModelScope }
        ?.launch { _effect.emit(CurrenciesEffect.FewCurrency) }

    private fun verifyCurrentBase() = settingsDataSource.currentBase.either(
        { isEmpty() },
        { base ->
            state.value.currencyList
                .filter { it.name == base }
                .toList().firstOrNull()?.isActive == false
        }
    )?.mapTo {
        state.value.currencyList.firstOrNull { it.isActive }?.name.orEmpty()
    }?.let { newBase ->
        settingsDataSource.currentBase = newBase

        analyticsManager.trackEvent(Event.BaseChange(Param.Base(newBase)))
        analyticsManager.setUserProperty(UserProperty.BaseCurrency(newBase))

        viewModelScope.launch { _effect.emit(CurrenciesEffect.ChangeBase(newBase)) }
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

    fun shouldShowBannerAd() = adRepository.shouldShowBannerAd()

    fun isFirstRun() = settingsDataSource.firstRun

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel updateAllCurrenciesState $state" }
        currencyDataSource.updateAllCurrencyState(state)
    }

    override fun onItemClick(currency: Currency) = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onItemClick ${currency.name}" }
        currencyDataSource.updateCurrencyStateByName(currency.name, !currency.isActive)
    }

    override fun onDoneClick() = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onDoneClick" }
        data.unFilteredList
            .filter { it.isActive }.size
            .whether { it < MINIMUM_ACTIVE_CURRENCY }
            ?.let { _effect.emit(CurrenciesEffect.FewCurrency) }
            ?: run {
                settingsDataSource.firstRun = false
                filterList("")
                _effect.emit(CurrenciesEffect.OpenCalculator)
            }
    }

    override fun onItemLongClick() = _state.value.selectionVisibility.let {
        Logger.d { "CurrenciesViewModel onItemLongClick" }
        _state.update(selectionVisibility = !it)
    }

    override fun onCloseClick() = viewModelScope.launchIgnored {
        Logger.d { "CurrenciesViewModel onCloseClick" }
        if (_state.value.selectionVisibility) {
            _state.update(selectionVisibility = false)
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

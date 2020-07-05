/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.model.Currencies
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.data.util.removeUnUsedCurrencies
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import com.github.mustafaozhan.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val currencyDao: CurrencyDao
) : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _state = MutableSettingsState()
    val state = SettingsState(_state)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData(preferencesRepository)

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        _state._loading.value = true

        viewModelScope.launch {
            currencyDao.getAllCurrencies()
                .map { it.removeUnUsedCurrencies() }
                .collect { currencyList ->

                    _state._currencyList.value = currencyList
                    data.unFilteredList = currencyList

                    currencyList
                        ?.filter { it.isActive }?.size
                        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                        ?.whetherNot { data.firstRun }
                        ?.let { _effect.postValue(FewCurrencyEffect) }

                    verifyCurrentBase()
                    filterList(data.query)
                    _state._selectionVisibility.value = false
                }
        }
        filterList("")
    }

    private fun verifyCurrentBase() = data.currentBase.either(
        { equals(Currencies.NULL.toString()) },
        { base ->
            state.currencyList.value
                ?.filter { it.name == base }
                ?.toList()?.firstOrNull()?.isActive == false
        }
    )?.let {
        updateCurrentBase(
            state.currencyList.value
                ?.firstOrNull { it.isActive }?.name
                ?: Currencies.NULL.toString()
        )
    }

    private fun updateCurrentBase(newBase: String) {
        data.currentBase = newBase
        _effect.postValue(ChangeBaseNavResultEffect(newBase))
    }

    fun filterList(txt: String) = data.unFilteredList
        ?.filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }?.toMutableList()
        ?.let {
            _state._currencyList.value = it
            _state._loading.value = false
        }.run {
            data.query = txt
            true
        }

    // region Event
    override fun updateAllCurrenciesState(state: Boolean) = viewModelScope.launch {
        currencyDao.updateAllCurrencyState(state)
    }.toUnit()

    override fun onItemClick(currency: Currency) = viewModelScope.launch {
        currencyDao.updateCurrencyStateByName(currency.name, !currency.isActive)
    }.toUnit()

    override fun onDoneClick() = _state._currencyList.value
        ?.filter { it.isActive }?.size
        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
        ?.let { _effect.postValue(FewCurrencyEffect) }
        ?: run {
            data.firstRun = false
            _effect.postValue(CalculatorEffect)
        }

    override fun onItemLongClick() = _state._selectionVisibility.value?.let {
        _state._selectionVisibility.value = !it
        true
    } ?: false

    override fun onBackClick() {
        _effect.postValue(BackEffect)
    }
    // endregion
}

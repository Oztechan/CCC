/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.android.util.MutableSingleLiveData
import com.github.mustafaozhan.ccc.android.util.SingleLiveData
import com.github.mustafaozhan.ccc.android.util.isRewardExpired
import com.github.mustafaozhan.ccc.android.util.toUnit
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.util.removeUnUsedCurrencies
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CurrenciesViewModel(
    private val settingsRepository: SettingsRepository,
    private val currencyDao: CurrencyDao
) : ViewModel(), CurrenciesEvent {

    // region SEED
    private val _state = MutableCurrenciesState()
    val state = CurrenciesState(_state)

    private val _effect = MutableSingleLiveData<CurrenciesEffect>()
    val effect: SingleLiveData<CurrenciesEffect> = _effect

    val data = CurrenciesData()

    fun getEvent() = this as CurrenciesEvent
    // endregion

    init {
        _state._loading.value = true

        viewModelScope.launch {
            currencyDao.collectAllCurrencies()
                .map { it.removeUnUsedCurrencies() }
                .collect { currencyList ->

                    _state._currencyList.value = currencyList
                    data.unFilteredList = currencyList

                    currencyList
                        ?.filter { it.isActive }?.size
                        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                        ?.whetherNot { settingsRepository.firstRun }
                        ?.let { _effect.postValue(FewCurrencyEffect) }

                    verifyCurrentBase()
                    filterList(data.query)
                    _state._selectionVisibility.value = false
                }
        }
        filterList("")
    }

    private fun verifyCurrentBase() = settingsRepository.currentBase.either(
        { equals(CurrencyType.NULL.toString()) },
        { base ->
            state.currencyList.value
                ?.filter { it.name == base }
                ?.toList()?.firstOrNull()?.isActive == false
        }
    )?.let {
        updateCurrentBase(
            state.currencyList.value
                ?.firstOrNull { it.isActive }?.name
                ?: CurrencyType.NULL.toString()
        )
    }

    private fun updateCurrentBase(newBase: String) {
        settingsRepository.currentBase = newBase
        _effect.postValue(ChangeBaseNavResultEffect(newBase))
    }

    fun hideSelectionVisibility() {
        _state._selectionVisibility.value = false
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

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    fun isFirstRun() = settingsRepository.firstRun

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
            settingsRepository.firstRun = false
            _effect.postValue(CalculatorEffect)
        }

    override fun onItemLongClick() = _state._selectionVisibility.value?.let {
        _state._selectionVisibility.value = !it
        true
    } ?: false

    override fun onCloseClick() = if (_state._selectionVisibility.value == true) {
        _state._selectionVisibility.value = false
    } else {
        _effect.postValue(BackEffect)
    }.run {
        filterList("")
    }.toUnit()
    // endregion
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.bar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.MutableSingleLiveData
import com.github.mustafaozhan.data.model.SingleLiveData
import com.github.mustafaozhan.data.util.removeUnUsedCurrencies
import com.github.mustafaozhan.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarViewModel
@Inject constructor(
    private val currencyDao: CurrencyDao
) : ViewModel(), BarEvent {
    // region SEED
    private val _state = MutableBarState()
    val state = BarState(_state)

    private val _effect = MutableSingleLiveData<BarEffect>()
    val effect: SingleLiveData<BarEffect> = _effect

    fun getEvent() = this as BarEvent
    // endregion

    init {
        with(_state) {
            viewModelScope.launch {
                currencyDao.collectActiveCurrencies()
                    .map { it.removeUnUsedCurrencies() }
                    .collect {
                        _currencyList.value = it
                        _loading.value = false
                        _enoughCurrency.postValue(it?.size ?: -1 >= MINIMUM_ACTIVE_CURRENCY)
                    }
            }
        }
    }

    // region Event
    override fun onItemClick(currency: Currency) = _effect.postValue(ChangeBaseNavResultEffect(currency.name))

    override fun onSelectClick() = _effect.postValue(OpenCurrenciesEffect)
    // endregion
}

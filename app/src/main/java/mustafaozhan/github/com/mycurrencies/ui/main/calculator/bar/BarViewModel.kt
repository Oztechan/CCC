/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.room.CurrencyDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies

class BarViewModel(
    private val currencyDao: CurrencyDao
) : BaseViewModel(), BarEvent {
    // region SEED
    private val _state = BarStateBacking()
    val state = BarState(_state)

    private val _effect = MutableSingleLiveData<BarEffect>()
    val effect: SingleLiveData<BarEffect> = _effect

    fun getEvent() = this as BarEvent
    // endregion

    init {
        with(_state) {
            viewModelScope.launch {
                currencyDao.getActiveCurrencies()
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

    override fun onSelectClick() = _effect.postValue(OpenSettingsEffect)
    // endregion
}

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
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies

class BarViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
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
                currencyRepository.getActiveCurrencies()
                    .map { it.removeUnUsedCurrencies() }
                    .collect {
                        _currencyList.value = it
                        _loading.value = false
                    }
            }
        }
    }

    // region Event
    override fun onItemClick(currency: Currency) {
        preferencesRepository.currentBase = currency.name
        _effect.value = BaseCurrencySelected
    }
    // endregion
}

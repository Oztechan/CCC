/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.inCase
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies

class SettingsViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : BaseViewModel(), SettingsEvent {

    // region SEED
    private val _states = SettingsStateBacking()
    val state = SettingsState(_states)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    val effect: SingleLiveData<SettingsEffect> = _effect

    val data = SettingsData()

    fun getEvent() = this as SettingsEvent
    // endregion

    init {
        initData()

        _states._searchQuery.addSource(state.searchQuery) {
            filterList(it)
        }

        viewModelScope.launch {
            currencyRepository.getAllCurrencies().collect { currencyList ->
                _states._currencyList.value = currencyList.removeUnUsedCurrencies()
                data.unFilteredList = currencyList

                currencyList
                    .filter { it.isActive }.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { preferencesRepository.firstRun }
                    ?.let { _effect.value = FewCurrencyEffect }

                verifyCurrentBase()
            }
        }

        filterList("")
    }

    private fun initData() = viewModelScope
        .whether { preferencesRepository.firstRun }
        ?.launch {
            _states._loading.value = true
            currencyRepository.insertInitialCurrencies()
            _states._loading.value = false
        }

    private fun filterList(txt: String) = data.unFilteredList
        .filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }.toMutableList()
        .let { _states._currencyList.value = it }

    private fun verifyCurrentBase() = preferencesRepository.currentBase.either(
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
    }.run {
        _states._searchQuery.value = ""
    }

    private fun updateCurrentBase(newBase: String) {
        preferencesRepository.currentBase = newBase
        _effect.postValue(ChangeBaseNavResultEffect(newBase))
    }

    // region Event
    override fun onSelectAllClick() = viewModelScope.launch {
        currencyRepository.updateAllCurrencyState(true)
    }.toUnit()

    override fun onDeselectAllClick() = viewModelScope.launch {
        currencyRepository.updateAllCurrencyState(false)
        updateCurrentBase(Currencies.NULL.toString())
    }.toUnit()

    override fun onItemClick(currency: Currency) = viewModelScope.launch {
        currency.isActive
            .mapTo { !it }.let { newState ->
                currencyRepository.updateCurrencyStateByName(currency.name, newState)
            }.inCase(currency.name == preferencesRepository.currentBase) {
                updateCurrentBase(Currencies.NULL.toString())
            }
    }.toUnit()

    override fun onDoneClick() = _states._currencyList.value
        ?.filter { it.isActive }?.size
        ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
        ?.let { _effect.value = FewCurrencyEffect }
        ?: run {
            preferencesRepository.firstRun = false
            _effect.value = CalculatorEffect
        }
    // endregion
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.lifecycle.MutableSingleLiveData
import com.github.mustafaozhan.basemob.lifecycle.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.SEEDViewModel
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.inCase
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsData
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEffect
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEvent
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsState
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsStateBacking

class SettingsViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : SEEDViewModel<SettingsState, SettingsEvent, SettingsEffect, SettingsData>(), SettingsEvent {

    // region SEED
    private val _states = SettingsStateBacking()
    override val state = SettingsState(_states)

    private val _effect = MutableSingleLiveData<SettingsEffect>()
    override val effect: SingleLiveData<SettingsEffect> = _effect

    override val data = SettingsData()

    override fun getEvent() = this as SettingsEvent
    // endregion

    init {
        initData()

        with(_states) {
            _searchQuery.addSource(state.searchQuery) {
                filterList(it)
            }
            _currencyList.addSource(currencyRepository.getAllCurrencies()) { currencyList ->
                _currencyList.value = currencyList.removeUnUsedCurrencies()
                data.unFilteredList = currencyList
                if (currencyList.filter { it.isActive }.size < MINIMUM_ACTIVE_CURRENCY) {
                    _effect.value = FewCurrencyEffect
                }
                verifyCurrentBase()
            }
        }

        filterList("")
    }

    private fun initData() = viewModelScope
        .whether { preferencesRepository.firstRun }
        ?.launch {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
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
        preferencesRepository.currentBase = state.currencyList.value
            ?.firstOrNull { it.isActive }?.name
            ?: Currencies.NULL.toString()
    }.run {
        _states._searchQuery.value = ""
    }

    // region Event
    override fun onSelectAllClick() {
        currencyRepository.updateAllCurrencyState(true)
    }

    override fun onDeselectAllClick() {
        currencyRepository.updateAllCurrencyState(false)
        preferencesRepository.currentBase = Currencies.NULL.toString()
    }

    override fun onItemClick(currency: Currency) = currency.isActive
        .mapTo { !it }.let { newState ->
            currencyRepository.updateCurrencyStateByName(currency.name, newState)
        }.inCase(currency.name == preferencesRepository.currentBase) {
            preferencesRepository.currentBase = Currencies.NULL.toString()
        }
    // endregion
}

package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.viewmodel.SEEDViewModel
import com.github.mustafaozhan.scopemob.either
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

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : SEEDViewModel<SettingsState, SettingsEvent, SettingsEffect, SettingsData>(), SettingsEvent {

    // region take it EASY!
    private val _states = SettingsStateBacking()
    override val state = SettingsState(_states)

    private val _events = MutableLiveData<SettingsEffect>()
    override val effect: LiveData<SettingsEffect> = _events

    override val event = this as SettingsEvent
    override val data = SettingsData()
    // endregion

    init {
        initData()

        _states.apply {
            _searchQuery.addSource(state.searchQuery) {
                filterList(it)
            }
            _currencyList.addSource(currencyRepository.getAllCurrencies()) { currencyList ->
                _currencyList.value = currencyList.removeUnUsedCurrencies()
                data.unFilteredList = currencyList
                if (currencyList.filter { it.isActive == 1 }.size < MINIMUM_ACTIVE_CURRENCY) {
                    _events.postValue(FewCurrencyEffect)
                }
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
        }
        .toMutableList()
        .let { _states._currencyList.value = it }

    private fun verifyCurrentBase() {
        preferencesRepository.currentBase
            .either(
                { equals(Currencies.NULL.toString()) },
                { base ->
                    state.currencyList.value
                        ?.filter { it.name == base }
                        ?.toList()?.firstOrNull()?.isActive == 0
                }
            )?.let {
                preferencesRepository.currentBase = state.currencyList.value
                    ?.firstOrNull { it.isActive == 1 }?.name
                    ?: Currencies.NULL.toString()
            }

        _states._searchQuery.value = ""
    }

    // region Event
    override fun onSelectDeselectButtonsClick(value: Int) {
        currencyRepository.updateAllCurrencyState(value)
        if (value == 0) {
            preferencesRepository.currentBase = Currencies.NULL.toString()
        }
        verifyCurrentBase()
    }

    override fun onItemClick(currency: Currency) = with(currency) {
        val newValue = if (isActive == 0) 1 else 0
        currencyRepository.updateCurrencyStateByName(name, newValue)
        if (currency.name == preferencesRepository.currentBase) {
            preferencesRepository.currentBase = Currencies.NULL.toString()
        }
        verifyCurrentBase()
    }
    // endregion
}

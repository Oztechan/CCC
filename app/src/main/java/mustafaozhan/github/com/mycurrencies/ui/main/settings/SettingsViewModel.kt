package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.viewmodel.EASYViewModel
import com.github.mustafaozhan.scopemob.either
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainYield.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.FewCurrencyEvent
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsAction
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEvent
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsState
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsStateBacking
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsYield

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : EASYViewModel<SettingsState, SettingsAction, SettingsEvent, SettingsYield>(), SettingsAction {

    // region SEED
    private val _state = SettingsStateBacking()
    override val state = SettingsState(_state)

    private val _event = MutableLiveData<SettingsEvent>()
    override val event: LiveData<SettingsEvent> = _event

    override val action = this as SettingsAction
    override val yield = SettingsYield()
    // endregion

    init {
        initData()

        _state.apply {
            _searchQuery.addSource(state.searchQuery) {
                filterList(it)
            }
            _currencyList.addSource(currencyRepository.getAllCurrencies()) { currencyList ->
                _currencyList.value = currencyList.removeUnUsedCurrencies()
                yield.unFilteredList = currencyList
                if (currencyList.filter { it.isActive == 1 }.size < MINIMUM_ACTIVE_CURRENCY) {
                    _event.postValue(FewCurrencyEvent)
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

    private fun filterList(txt: String) = yield.unFilteredList
        .filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }
        .toMutableList()
        .let { _state._currencyList.value = it }

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

        _state._searchQuery.value = ""
    }

    // region View Event
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

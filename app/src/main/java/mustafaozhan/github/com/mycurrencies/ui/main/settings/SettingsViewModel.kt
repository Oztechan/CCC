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
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsData
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEffect
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEvent
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsState
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model._SettingsState

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : SEEDViewModel<SettingsState, SettingsEvent, SettingsEffect, SettingsData>(), SettingsEvent {

    companion object {
        private const val MINIMUM_ACTIVE_CURRENCY = 2
    }

    private val _state = _SettingsState()
    override val state = SettingsState(_state)

    private val _effect = MutableLiveData<SettingsEffect>()
    override val effect: LiveData<SettingsEffect> = _effect

    override val event = this as SettingsEvent
    override val data = SettingsData()

    init {
        initData()

        _state.apply {
            _searchQuery.addSource(state.searchQuery) {
                filterList(it)
            }
            _currencyList.addSource(currencyRepository.getAllCurrencies()) {
                _currencyList.value = it.removeUnUsedCurrencies()
                data.unFilteredList = it
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

        if (state.currencyList.value?.filter { it.isActive == 1 }?.size ?: -1 < MINIMUM_ACTIVE_CURRENCY) {
            _effect.postValue(FewCurrency)
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
        verifyCurrentBase()
    }
    // endregion
}

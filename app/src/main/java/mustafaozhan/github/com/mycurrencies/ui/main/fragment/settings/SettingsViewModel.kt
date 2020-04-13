package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.viewmodel.SEEDViewModel
import com.github.mustafaozhan.scopemob.either
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.SettingsData
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.SettingsEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.SettingsEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.SettingsState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model.SettingsViewStateObserver

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : SEEDViewModel<SettingsState, SettingsEvent, SettingsEffect, SettingsData>(), SettingsEvent {

    companion object {
        private const val MINIMUM_ACTIVE_CURRENCY = 2
    }

    override val state = SettingsState(SettingsViewStateObserver())
    override val event = this as SettingsEvent
    override val effect = MutableLiveData<SettingsEffect>()
    override val data = SettingsData(preferencesRepository)

    init {
        initData()

        state.observer.searchQuery.addSource(state.searchQuery) {
            filterList(it)
        }
        filterList("")
    }

    private fun initData() {
        state.currencyList.value?.clear()

        if (preferencesRepository.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyRepository.getAllCurrencies().removeUnUsedCurrencies()?.let {
            state.currencyList.value?.addAll(it)
        }
    }

    private fun filterList(txt: String) = state.currencyList.value
        ?.filter { (name, longName, symbol) ->
            name.contains(txt, true) ||
                longName.contains(txt, true) ||
                symbol.contains(txt, true)
        }
        ?.toMutableList()
        .let {
            if (it?.isEmpty() == true) {
                state.noResult.postValue(true)
            } else {
                state.currencyList.postValue(it)
            }
        }

    private fun verifyCurrentBase(value: Int) {
        if (value == 0) {
            preferencesRepository.currentBase
                .either(
                    { equals(Currencies.NULL) },
                    { base ->
                        state.currencyList.value
                            ?.filter { it.name == base.toString() }
                            ?.toList()?.firstOrNull()?.isActive == 0
                    }
                )?.let {
                    preferencesRepository.setCurrentBase(
                        state.currencyList.value?.firstOrNull { it.isActive == 1 }?.name
                    )
                }
        }

        if (state.currencyList.value?.filter { it.isActive == 1 }?.size ?: -1 < MINIMUM_ACTIVE_CURRENCY) {
            effect.postValue(FewCurrency)
        }

        state.searchQuery.postValue("")
    }

    // region View Event
    override fun onSelectDeselectButtonsClick(value: Int) {
        state.currencyList.value?.forEach { it.isActive = value }
        currencyRepository.updateAllCurrencyState(value)
        verifyCurrentBase(value)
        if (value == 0) {
            preferencesRepository.setCurrentBase(null)
        }
    }

    override fun onItemClick(currency: Currency) = with(currency) {
        val newValue = if (isActive == 0) 1 else 0
        state.currencyList.value
            ?.find { (nameToFilter) -> nameToFilter == name }
            ?.isActive = newValue
        currencyRepository.updateCurrencyStateByName(name, newValue)
        verifyCurrentBase(newValue)
    }
    // endregion
}

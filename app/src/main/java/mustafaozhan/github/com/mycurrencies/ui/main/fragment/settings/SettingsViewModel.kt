package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.scopemob.either
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.DataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsData
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewStateObserver

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : DataViewModel<SettingsState, SettingsEvent, SettingsEffect, SettingsData>(
    preferencesRepository
), SettingsEvent {

    // region SEED
    override val state: SettingsState
        get() = SettingsState(SettingsViewStateObserver())
    override val event: SettingsEvent
        get() = this
    override val effect: MutableLiveData<SettingsEffect>
        get() = MutableLiveData()
    override val data: SettingsData
        get() = SettingsData()
    // endregion

    init {
        initData()

        state.observer.searchQuery.addSource(state.searchQuery) {
            filterList(it)
        }
        filterList("")
    }

    private fun initData() {
        state.currencyList.value?.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyRepository.getAllCurrencies().removeUnUsedCurrencies()?.let {
            state.currencyList.value?.addAll(it)
        }
    }

    private fun filterList(txt: String) = state.currencyList.value
        ?.filter { currency ->
            currency.name.contains(txt, true) ||
                currency.longName.contains(txt, true) ||
                currency.symbol.contains(txt, true)
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
            mainData.currentBase
                .either(
                    { equals(Currencies.NULL) },
                    { base ->
                        state.currencyList.value
                            ?.filter { it.name == base.toString() }
                            ?.toList()?.firstOrNull()?.isActive == 0
                    }
                )?.let { setCurrentBase(state.currencyList.value?.firstOrNull { it.isActive == 1 }?.name) }
        }

        if (state.currencyList.value?.filter { it.isActive == 1 }?.size ?: -1 < MINIMUM_ACTIVE_CURRENCY) {
            effect.postValue(FewCurrency)
        }

        state.searchQuery.postValue("")
    }

    override fun currentBaseChanged(newBase: String) = Unit

    // region View Event
    override fun onSelectDeselectButtonsClick(value: Int) {
        state.currencyList.value?.forEach { it.isActive = value }
        currencyRepository.updateAllCurrencyState(value)
        verifyCurrentBase(value)
        if (value == 0) {
            setCurrentBase(null)
        }
    }

    override fun onItemClick(currency: Currency) = with(currency) {
        val newValue = if (isActive == 0) 1 else 0
        state.currencyList.value?.find { it -> it.name == name }?.isActive = newValue
        currencyRepository.updateCurrencyStateByName(name, newValue)
        verifyCurrentBase(newValue)
    }
    // endregion
}

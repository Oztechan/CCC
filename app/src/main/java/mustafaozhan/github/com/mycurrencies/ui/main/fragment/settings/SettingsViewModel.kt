package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.scopemob.either
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewStateWrapper

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : MainDataViewModel<SettingsViewEffect, SettingsViewEvent, SettingsViewState>(
    preferencesRepository
), SettingsViewEvent {

    override val viewState = SettingsViewState(SettingsViewStateWrapper())
    override val viewEffectLiveData: MutableLiveData<SettingsViewEffect> = MutableLiveData()
    override fun getViewEvent() = this as SettingsViewEvent

    init {
        initData()

        viewState.wrapper.searchQuery.addSource(
            viewState.searchQuery
        ) {
            filterList(it)
        }
        filterList("")
    }

    private fun initData() {
        viewState.currencyList.value?.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyRepository.getAllCurrencies().removeUnUsedCurrencies()?.let {
            viewState.currencyList.value?.addAll(it)
        }
    }

    private fun filterList(txt: String) = viewState.currencyList.value
        ?.filter { currency ->
            currency.name.contains(txt, true) ||
                currency.longName.contains(txt, true) ||
                currency.symbol.contains(txt, true)
        }
        ?.toMutableList()
        .let {
            if (it?.isEmpty() == true) {
                viewState.noResult.postValue(true)
            } else {
                viewState.currencyList.postValue(it)
            }
        }

    private fun verifyCurrentBase(value: Int) {
        if (value == 0) {
            mainData.currentBase
                .either(
                    { equals(Currencies.NULL) },
                    { base ->
                        viewState.currencyList.value
                            ?.filter { it.name == base.toString() }
                            ?.toList()?.firstOrNull()?.isActive == 0
                    }
                )?.let { setCurrentBase(viewState.currencyList.value?.firstOrNull { it.isActive == 1 }?.name) }
        }

        if (viewState.currencyList.value?.filter { it.isActive == 1 }?.size ?: -1 < MINIMUM_ACTIVE_CURRENCY) {
            viewEffectLiveData.postValue(FewCurrency)
        }

        if (value == 0) {
            setCurrentBase(null)
        }

        viewState.searchQuery.postValue("")
    }

    override fun updateAllStates(value: Int) {
        viewState.currencyList.value?.forEach { it.isActive = value }
        currencyRepository.updateAllCurrencyState(value)
        verifyCurrentBase(value)
    }

    override fun updateCurrencyState(value: Int, name: String) {
        viewState.currencyList.value?.find { it.name == name }?.isActive = value
        currencyRepository.updateCurrencyStateByName(name, value)
        verifyCurrentBase(value)
    }
}

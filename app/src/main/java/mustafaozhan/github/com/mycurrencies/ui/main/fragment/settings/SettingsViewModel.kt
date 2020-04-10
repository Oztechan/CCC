package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.scopemob.either
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.FewCurrency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.NoResult
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.Success

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    preferencesRepository: PreferencesRepository,
    private val currencyRepository: CurrencyRepository
) : MainDataViewModel<SettingsViewEffect, SettingsViewEvent, SettingsViewState>(
    preferencesRepository
), SettingsViewEvent {

    override val viewStateLiveData: MutableLiveData<SettingsViewState> = MutableLiveData()
    override val viewEffectLiveData: MutableLiveData<SettingsViewEffect> = MutableLiveData()

    private val searchQueryMediatorLiveData = MediatorLiveData<String>()
    val searchQueryLiveData: MutableLiveData<String> = searchQueryMediatorLiveData

    private val currencyList: MutableList<Currency> = mutableListOf()

    init {
        initData()

        searchQueryMediatorLiveData.addSource(searchQueryLiveData) {
            filterList(it)
        }
        filterList("")
    }

    private fun initData() {
        currencyList.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyRepository.getAllCurrencies().removeUnUsedCurrencies()?.let {
            currencyList.addAll(it)
        }
    }

    private fun filterList(txt: String) = currencyList
        .filter { currency ->
            currency.name.contains(txt, true) ||
                currency.longName.contains(txt, true) ||
                currency.symbol.contains(txt, true)
        }
        .toMutableList()
        .let {
            viewStateLiveData.postValue(if (it.isEmpty()) NoResult else Success(it))
        }

    private fun verifyCurrentBase(value: Int) {
        if (value == 0) {
            mainData.currentBase
                .either(
                    { equals(Currencies.NULL) },
                    { base ->
                        currencyList
                            .filter { it.name == base.toString() }
                            .toList().firstOrNull()?.isActive == 0
                    }
                )?.let { setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name) }
        }

        if (currencyList.filter { it.isActive == 1 }.size < MINIMUM_ACTIVE_CURRENCY) {
            viewEffectLiveData.postValue(FewCurrency)
        }

        if (value == 0) {
            setCurrentBase(null)
        }

        searchQueryLiveData.postValue("")
    }

    override fun updateAllStates(value: Int) {
        currencyList.forEach { it.isActive = value }
        currencyRepository.updateAllCurrencyState(value)
        verifyCurrentBase(value)
    }

    override fun updateCurrencyState(value: Int, name: String) {
        currencyList.find { it.name == name }?.isActive = value
        currencyRepository.updateCurrencyStateByName(name, value)
        verifyCurrentBase(value)
    }

    override fun getViewEvent() = this as SettingsViewEvent
}

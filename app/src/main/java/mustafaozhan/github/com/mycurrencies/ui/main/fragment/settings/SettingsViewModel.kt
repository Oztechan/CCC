package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.scopemob.either
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.extension.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsViewModel(
    preferencesRepository: PreferencesRepository,
    private val currencyDao: CurrencyDao
) : MainDataViewModel(preferencesRepository) {

    private val _searchQuery = MediatorLiveData<String>()
    private val currencyList: MutableList<Currency> = mutableListOf()

    val searchQuery: MutableLiveData<String> = _searchQuery
    val settingsViewStateLiveData: MutableLiveData<SettingsViewState> = MutableLiveData()

    init {
        initData()

        _searchQuery.addSource(searchQuery) {
            filterList(it)
        }
        filterList("")
    }

    private fun initData() {
        currencyList.clear()

        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyDao.getAllCurrencies().removeUnUsedCurrencies()?.let {
            currencyList.addAll(it)
        }
    }

    fun updateCurrencyState(value: Int, txt: String? = null) {
        txt?.let { name ->
            currencyList.find { it.name == name }?.isActive = value
            currencyDao.updateCurrencyStateByName(name, value)
        } ?: updateAllCurrencyState(value)

        if (value == 0) verifyCurrentBase()

        if (currencyList.filter { it.isActive == 1 }.size < MINIMUM_ACTIVE_CURRENCY) {
            settingsViewStateLiveData.postValue(SettingsViewState.FewCurrency)
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
            settingsViewStateLiveData.postValue(
                if (it.isEmpty()) SettingsViewState.NoResult else SettingsViewState.Success(it)
            )
        }

    private fun updateAllCurrencyState(value: Int) {
        currencyList.forEach { it.isActive = value }
        currencyDao.updateAllCurrencyState(value)
    }

    private fun verifyCurrentBase() = mainData.currentBase
        .either(
            { equals(Currencies.NULL) },
            { base ->
                currencyList
                    .filter { it.name == base.toString() }
                    .toList().firstOrNull()?.isActive == 0
            }
        )?.let { setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name) }
}

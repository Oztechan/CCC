package mustafaozhan.github.com.mycurrencies.settings

import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var currencyDao: CurrencyDao

    var currencyList: MutableList<Currency> = mutableListOf()

    fun refreshData() {
        loadPreferences()
        currencyList.clear()
        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }
        currencyList.addAll(currencyDao.getAllCurrencies())
    }

    fun updateCurrency(currency: Currency) = currencyDao.updateCurrency(currency)

    fun updateCurrencyState(value: Int) {
        currencyList.forEach { currency ->
            currency.isActive = value
            currencyDao.updateCurrency(currency)
        }
        if (value == 0) {
            verifyCurrentBase()
        }
    }

    fun verifyCurrentBase() = currencyList.let { currencyList ->
        if (mainData.currentBase == Currencies.NULL ||
            currencyList.filter { it.name == mainData.currentBase.toString() }.toList().first().isActive == 0) {
            setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
        }
    }
}
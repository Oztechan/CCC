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

    fun updateCurrencyState(value: Int, txt: String? = null) {
        txt?.let { name ->
            currencyList.find { it.name == name }?.isActive = value
            currencyDao.updateCurrencyStateByName(name, value)
        } ?: updateAllCurrencyState(value)

        if (value == 0) verifyCurrentBase()
    }

    private fun updateAllCurrencyState(value: Int) {
        currencyList.forEach { it.isActive = value }
        currencyDao.updateAllCurrencyState(value)
    }

    private fun verifyCurrentBase() = currencyList.let { currencyList ->
        if (mainData.currentBase == Currencies.NULL ||
            currencyList.filter { it.name == mainData.currentBase.toString() }.toList().first().isActive == 0) {
            setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
        }
    }
}
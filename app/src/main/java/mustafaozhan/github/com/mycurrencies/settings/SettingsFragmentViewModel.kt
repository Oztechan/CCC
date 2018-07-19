package mustafaozhan.github.com.mycurrencies.settings

import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import mustafaozhan.github.com.mycurrencies.tools.insertInitialCurrencies
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


    fun initData() {
        currencyList.clear()
        if (dataManager.firstTime) {
            currencyDao.insertInitialCurrencies()
            dataManager.firstTime = false
        }
        currencyDao.getAllCurrencies().forEach {
            currencyList.add(it)
        }
    }

    fun getCurrentBase() = dataManager.currentBase
    fun getBaseCurrency() = dataManager.baseCurrency

    fun setBaseCurrency(newBase: String) {
        dataManager.baseCurrency = Currencies.valueOf(newBase)
    }

    fun updateCurrencyStateByName(name: String, i: Int) {
        currencyDao.updateCurrencyStateByName(name, i)
    }

    fun updateAllCurrencyState(value: Int) {
        currencyDao.updateAllCurrencyState(value)
    }
}
package mustafaozhan.github.com.mycurrencies.data.room.currency

import mustafaozhan.github.com.mycurrencies.extension.insertInitialCurrencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject
constructor(private val currencyDao: CurrencyDao) {

    fun getActiveCurrencies() = currencyDao.getActiveCurrencies()

    fun getCurrencyByName(name: String) = currencyDao.getCurrencyByName(name)

    fun insertInitialCurrencies() = currencyDao.insertInitialCurrencies()

    fun getAllCurrencies() = currencyDao.getAllCurrencies()

    fun updateCurrencyStateByName(name: String, value: Int) = currencyDao.updateCurrencyStateByName(name, value)

    fun updateAllCurrencyState(value: Int) = currencyDao.updateAllCurrencyState(value)
}

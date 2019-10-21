package mustafaozhan.github.com.mycurrencies.settings

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.tools.DataManager

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragmentViewModel(
    dataManager: DataManager,
    val currencyDao: CurrencyDao
) : BaseViewModel(dataManager) {

    var currencyList: MutableList<Currency> = mutableListOf()

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun refreshData() {
        loadPreferences()
        currencyList.clear()

        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
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
    }

    private fun updateAllCurrencyState(value: Int) {
        currencyList.forEach { it.isActive = value }
        currencyDao.updateAllCurrencyState(value)
    }

    private fun verifyCurrentBase() = currencyList.let { currencyList ->
        if (mainData.currentBase == Currencies.NULL ||
            currencyList
                .filter { it.name == mainData.currentBase.toString() }
                .toList().firstOrNull()?.isActive == 0
        ) {
            setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
        }
    }
}

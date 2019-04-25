package mustafaozhan.github.com.mycurrencies.settings

import android.arch.lifecycle.MutableLiveData
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

    var currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()

    fun refreshData() {
        loadPreferences()
        currencyListLiveData.value?.clear()
        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }
        currencyListLiveData.postValue(currencyDao.getAllCurrencies())
    }

    fun updateCurrencyState(txt: String?, value: Int) {
        txt?.let { name ->
            currencyListLiveData.value?.find { it.name == name }?.isActive = value
            currencyDao.updateCurrencyStateByName(name, value)
        } ?: updateAllCurrencyState(value)

        if (value == 0) {
            verifyCurrentBase()
        }
    }

    private fun updateAllCurrencyState(value: Int) {
        currencyListLiveData.value?.forEach { it.isActive = value }
        currencyDao.updateAllCurrencyState(value)
    }

    private fun verifyCurrentBase() {
        currencyListLiveData.value?.let { currencyList ->
            if (mainData.currentBase == Currencies.NULL ||
                currencyList.filter { it.name == mainData.currentBase.toString() }.toList().first().isActive == 0) {
                setCurrentBase(currencyList.firstOrNull { it.isActive == 1 }?.name)
            }
        }
    }
}
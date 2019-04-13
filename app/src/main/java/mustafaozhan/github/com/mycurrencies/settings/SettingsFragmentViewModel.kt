package mustafaozhan.github.com.mycurrencies.settings

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.model.MainData
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

    var originalList = mutableListOf<Currency>()

    val filteredListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()

    lateinit var mainData: MainData

    fun initData() {
        originalList.clear()
        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }
        currencyDao.getAllCurrencies().let { list ->
            originalList = list
            filteredListLiveData.postValue(list)
        }
    }

    fun setCurrentBase(newBase: String?) {
        mainData.currentBase = Currencies.valueOf(newBase ?: "NULL")
        dataManager.persistMainData(mainData)
    }

    fun updateCurrencyStateByName(name: String, i: Int) {
        currencyDao.updateCurrencyStateByName(name, i)
    }

    fun updateAllCurrencyState(value: Int) {
        originalList.forEach { it.isActive = value }
        currencyDao.updateAllCurrencyState(value)
    }

    fun loadPreferences() {
        mainData = dataManager.loadMainData()
    }

    fun savePreferences() {
        dataManager.persistMainData(mainData)
    }

    fun search(query: String) {
        val wanted = originalList.filter { currency ->
            currency.name.contains(query, true) ||
                currency.longName.contains(query, true) ||
                currency.symbol.contains(query, true)
        }.toMutableList()

        filteredListLiveData.postValue(wanted)
    }
}
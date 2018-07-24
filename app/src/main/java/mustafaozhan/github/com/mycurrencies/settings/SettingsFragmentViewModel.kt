package mustafaozhan.github.com.mycurrencies.settings

import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.base.model.MainData
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.toOfflineRates
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
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

    @Inject
    lateinit var offlineRatesDao: OfflineRatesDao

    var currencyList: MutableList<Currency> = mutableListOf()

    lateinit var mainData: MainData

    fun initData() {
        currencyList.clear()
        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }

        currencyDao.getAllCurrencies().forEach {
            currencyList.add(it)
        }

    }


    fun setBaseCurrency(newBase: String?) {
        if (newBase == null)
            mainData.baseCurrency = Currencies.NULL
        else {
            mainData.baseCurrency = Currencies.valueOf(newBase.toString())
            mainData.currentBase = Currencies.valueOf(newBase.toString())
        }
        dataManager.persistMainData(mainData)
    }

    fun updateCurrencyStateByName(name: String, i: Int) {
        currencyDao.updateCurrencyStateByName(name, i)
        if (i == 1)
            updateOfflineRateByName(name)
    }


    fun updateAllCurrencyState(value: Int) {
        currencyList.forEach {
            it.isActive = value
            updateOfflineRateByName(it.name)
        }
        currencyDao.updateAllCurrencyState(value)
    }

    fun loadPreferences() {
        mainData = dataManager.loadMainData()

    }

    fun savePreferences() {
        dataManager.persistMainData(mainData)
    }

    private fun updateOfflineRateByName(name: String) {
        subscribeService(dataManager.getAllOnBase(Currencies.valueOf(name)),
                ::offlineRateByNameSuccess, ::eventDownloadByNameFail)
    }

    private fun eventDownloadByNameFail(throwable: Throwable) {
        if (throwable.message != "Unable to resolve host \"exchangeratesapi.io\": No address associated with hostname")
            throwable.printStackTrace()
    }

    private fun offlineRateByNameSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.toOfflineRates()?.let { offlineRatesDao.updateOfflineRates(it) }
    }
}
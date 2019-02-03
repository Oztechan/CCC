package mustafaozhan.github.com.mycurrencies.settings

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.toOfflineRates
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.MainData
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

    val currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()

    lateinit var mainData: MainData

    fun initData() {
        currencyListLiveData.value?.clear()
        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }
        currencyListLiveData.postValue(currencyDao.getAllCurrencies())
    }


    fun setCurrentBase(newBase: String?) {
        if (newBase == null) {
            mainData.currentBase = Currencies.NULL
        } else {
            mainData.currentBase = Currencies.valueOf(newBase.toString())
        }
        dataManager.persistMainData(mainData)
    }

    fun updateCurrencyStateByName(name: String, i: Int) {
        currencyDao.updateCurrencyStateByName(name, i)
        if (i == 1) {
            updateOfflineRateByName(name)
        }
    }


    fun updateAllCurrencyState(value: Int) {
        currencyListLiveData.value?.forEach { it.isActive = value }
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
        if (throwable.message != "Unable to resolve host \"exchangeratesapi.io\": No address associated with hostname") {
            throwable.printStackTrace()
        }
    }

    private fun offlineRateByNameSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.toOfflineRates().let { offlineRatesDao.insertOfflineRates(it) }
    }
}
package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.base.model.MainData
import mustafaozhan.github.com.mycurrencies.extensions.getRates
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.toOfflineRates
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.mariuszgromada.math.mxparser.Expression
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var currencyDao: CurrencyDao

    @Inject
    lateinit var offlineRatesDao: OfflineRatesDao

    val currenciesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyList: MutableList<Currency> = mutableListOf()

    lateinit var mainData: MainData

    var input: String = ""
    var output: String = "0.0"


    fun refreshData() {
        currencyList.clear()

        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            for (i in 0 until Currencies.values().size - 1)
                subscribeService(dataManager.getAllOnBase(Currencies.values()[i]),
                        ::offlineRateAllSuccess, ::offlineRateAllFail)
            mainData.firstRun = false
        }

        currencyDao.getActiveCurrencies().forEach {
            currencyList.add(it)
        }
    }


    fun loadPreferences() {
        mainData = dataManager.loadMainData()
    }

    fun savePreferences() {
        dataManager.persistMainData(mainData)
    }

    fun getCurrencies() {
        subscribeService(dataManager.getAllOnBase(mainData.currentBase),
                ::rateDownloadSuccess, ::rateDownloadFail)

    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.rates
        currenciesLiveData.postValue(currencyResponse.rates)


        currencyResponse.toOfflineRates().let {
            offlineRatesDao.updateOfflineRates(it)
        }

    }

    private fun rateDownloadFail(t: Throwable) {
        if (t.message != "Unable to resolve host \"exchangeratesapi.io\": No address associated with hostname")
            t.printStackTrace()
        try {
            currenciesLiveData.postValue(offlineRatesDao.getOfflineRatesOnBase(mainData.currentBase.toString()).getRates())
        } catch (e: Exception) {
            e.printStackTrace()//first run without internet
        }
    }

    private fun offlineRateAllFail(throwable: Throwable) {
        if (throwable.message != "Unable to resolve host \"exchangeratesapi.io\": No address associated with hostname")
            throwable.printStackTrace()

    }

    private fun offlineRateAllSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.toOfflineRates().let { offlineRatesDao.insertOfflineRates(it) }

    }

    fun calculate(text: String?): String {
        var result: String? = null

        if (text != null) {
            result = if (text.contains("%"))
                Expression(text.replace("%", "/100*")).calculate().toString()
            else
                Expression(text).calculate().toString()
        }

        return result.toString()
    }
}





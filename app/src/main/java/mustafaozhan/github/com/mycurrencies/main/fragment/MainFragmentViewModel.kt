package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.calculateResultByCurrency
import mustafaozhan.github.com.mycurrencies.extensions.getRates
import mustafaozhan.github.com.mycurrencies.extensions.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.replaceCommas
import mustafaozhan.github.com.mycurrencies.extensions.toOfflineRates
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var currencyDao: CurrencyDao

    @Inject
    lateinit var offlineRatesDao: OfflineRatesDao

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()
    var rates: Rates? = null
    lateinit var mainData: MainData
    var output: String = "0.0"

    fun refreshData() {
        currencyListLiveData.value?.clear()

        if (mainData.firstRun) {
            insertInitialCurrencies()
            for (i in 0 until Currencies.values().size - 1) {
                subscribeService(dataManager.backendGetAllOnBase(Currencies.values()[i]),
                    ::offlineRateAllSuccess, ::offlineRateAllFail)
            }
            mainData.firstRun = false
        }
        currencyListLiveData.postValue(currencyDao.getActiveCurrencies())
    }

    fun insertInitialCurrencies() {
        currencyDao.insertInitialCurrencies()
        for (i in 0 until Currencies.values().size - 1) {
            subscribeService(dataManager.backendGetAllOnBase(Currencies.values()[i]),
                ::offlineRateAllSuccess, ::offlineRateAllFail)
        }
        persistResetData(false)
    }

    fun loadPreferences() {
        mainData = dataManager.loadMainData()
    }

    fun savePreferences() {
        dataManager.persistMainData(mainData)
    }

    @Suppress("ComplexMethod")
    fun getCurrencies() {
        if (rates != null) {
            rates.let { rates ->
                currencyListLiveData.value?.forEach { currency ->
                    currency.rate = calculateResultByCurrency(currency.name, output, rates)
                }
                ratesLiveData.postValue(rates)
            }
        } else {
            subscribeService(dataManager.backendGetAllOnBase(mainData.currentBase),
                ::rateDownloadSuccess,
                ::backendRateDownloadFail)
        }
    }

    private fun backendRateDownloadFail(t: Throwable) {
        t.printStackTrace()
        if (mainData.currentBase == Currencies.BTC) {
            subscribeService(dataManager.exchangesRatesGetAllOnBase(Currencies.CRYPTO_BTC),
                ::rateDownloadSuccess,
                ::rateDownloadFail)
        } else {
            subscribeService(dataManager.exchangesRatesGetAllOnBase(mainData.currentBase),
                ::rateDownloadSuccess,
                ::rateDownloadFail)
        }
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) {
        ratesLiveData.postValue(currencyResponse.rates)
        rates = currencyResponse.rates
        currencyResponse.toOfflineRates().let {
            offlineRatesDao.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        t.printStackTrace()
        offlineRatesDao.getOfflineRatesOnBase(mainData.currentBase.toString()).let { offlineRates ->
            ratesLiveData.postValue(offlineRates?.getRates())
        }
    }

    fun calculateOutput(text: String) {
        val calculation = Expression(
            text.replaceCommas()
                .replace("%", "/100*")
        ).calculate()

        output = if (calculation.isNaN()) {
            ""
        } else {
            DecimalFormat("0.000")
                .format(calculation)
        }
    }

    fun updateCurrentBase(currency: String?) {
        mainData.currentBase = Currencies.valueOf(currency ?: "NULL")
        savePreferences()
    }

    fun loadResetData() = dataManager.loadResetData()

    private fun persistResetData(resetData: Boolean) = dataManager.persistResetData(resetData)

    fun getClickedItemRate(name: String): String {
        return "1 ${mainData.currentBase.name} = ${rates?.getThroughReflection<Double>(name)}"
    }

    private fun offlineRateAllFail(throwable: Throwable) {
        if (throwable.message !=
            "Unable to resolve host \"exchangeratesapi.io\": No address associated with hostname") {
            throwable.printStackTrace()
        }
    }

    private fun offlineRateAllSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.toOfflineRates().let { offlineRatesDao.insertOfflineRates(it) }
    }
}
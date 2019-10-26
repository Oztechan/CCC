package mustafaozhan.github.com.mycurrencies.main.fragment.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.DataManager
import mustafaozhan.github.com.mycurrencies.extensions.calculateResult
import mustafaozhan.github.com.mycurrencies.extensions.getFormatted
import mustafaozhan.github.com.mycurrencies.extensions.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extensions.insertInitialCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.extensions.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extensions.replaceUnsupportedCharacters
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import org.mariuszgromada.math.mxparser.Expression

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class MainFragmentViewModel(
    override val dataManager: DataManager,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseViewModel() {

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()
    var rates: Rates? = null
    var output: String = "0.0"

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun refreshData() {
        loadPreferences()
        currencyListLiveData.value?.clear()

        if (mainData.firstRun) {
            currencyDao.insertInitialCurrencies()
            mainData.firstRun = false
        }

        currencyListLiveData.postValue(currencyDao.getActiveCurrencies().removeUnUsedCurrencies())
    }

    fun getCurrencies() {
        rates?.let { rates ->
            currencyListLiveData.value?.forEach { currency ->
                currency.rate = calculateResultByCurrency(currency.name, output, rates)
            }
            ratesLiveData.postValue(rates)
        } ?: run {
            subscribeService(
                dataManager.backendGetAllOnBase(mainData.currentBase),
                ::rateDownloadSuccess,
                ::rateDownloadFail
            )
        }
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) {
        rates = currencyResponse.rates
        rates?.base = currencyResponse.base
        rates?.let {
            ratesLiveData.postValue(it)
            offlineRatesDao.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        Crashlytics.logException(t)
        Crashlytics.log(Log.WARN, "rateDownloadFail", t.message)
        offlineRatesDao.getOfflineRatesOnBase(mainData.currentBase.toString()).let { offlineRates ->
            ratesLiveData.postValue(offlineRates)
        }
    }

    fun calculateOutput(text: String) {
        val calculation = Expression(
            text.replaceUnsupportedCharacters()
                .replace("%", "/100*")
        ).calculate()

        output = if (calculation.isNaN()) {
            ""
        } else {
            calculation.getFormatted()
        }
    }

    fun updateCurrentBase(currency: String?) {
        rates = null
        setCurrentBase(currency)
    }

    fun loadResetData() = dataManager.loadResetData()

    fun persistResetData(resetData: Boolean) = dataManager.persistResetData(resetData)

    fun getClickedItemRate(name: String): String =
        "1 ${mainData.currentBase.name} = ${rates?.getThroughReflection<Double>(name)}"

    fun getCurrencyByName(name: String) = currencyDao.getCurrencyByName(name)

    fun verifyCurrentBase(spinnerList: List<String>): Currencies {
        if (mainData.currentBase == Currencies.NULL || spinnerList.indexOf(mainData.currentBase.toString()) == -1) {
            updateCurrentBase(currencyListLiveData.value?.firstOrNull { it.isActive == 1 }?.name)
        }

        return mainData.currentBase
    }

    fun calculateResultByCurrency(name: String, value: String, rate: Rates?) =
        if (value.isNotEmpty()) {
            try {
                rate.calculateResult(name, value)
            } catch (e: NumberFormatException) {
                val numericValue = value.replaceUnsupportedCharacters().replaceNonStandardDigits()
                Crashlytics.logException(e)
                Crashlytics.log(Log.ERROR,
                    "NumberFormatException $value to $numericValue",
                    "If no crash making numeric is done successfully"
                )
                rate.calculateResult(name, numericValue)
            }
        } else {
            0.0
        }
}

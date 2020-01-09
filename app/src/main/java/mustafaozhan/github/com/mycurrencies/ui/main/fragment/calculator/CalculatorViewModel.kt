package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseDataViewModel
import mustafaozhan.github.com.mycurrencies.data.repository.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
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
class CalculatorViewModel(
    preferencesRepository: PreferencesRepository,
    private val backendRepository: BackendRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseDataViewModel(preferencesRepository) {

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()
    var rates: Rates? = null
    var output: String = "0.0"

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun refreshData() {
        currencyListLiveData.value?.clear()

        if (getMainData().firstRun) {
            currencyDao.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
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
                backendRepository.getAllOnBase(getMainData().currentBase),
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
        offlineRatesDao.getOfflineRatesOnBase(getMainData().currentBase.toString()).let { offlineRates ->
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

    fun loadResetData() = preferencesRepository.loadResetData()

    fun persistResetData(resetData: Boolean) = preferencesRepository.persistResetData(resetData)

    fun getClickedItemRate(name: String): String =
        "1 ${getMainData().currentBase.name} = ${rates?.getThroughReflection<Double>(name)}"

    fun getCurrencyByName(name: String) = currencyDao.getCurrencyByName(name)

    fun verifyCurrentBase(spinnerList: List<String>): Currencies {
        if (getMainData().currentBase == Currencies.NULL ||
            spinnerList.indexOf(getMainData().currentBase.toString()) == -1) {
            updateCurrentBase(currencyListLiveData.value?.firstOrNull { it.isActive == 1 }?.name)
        }

        return getMainData().currentBase
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

    fun resetFirstRun() {
        preferencesRepository.updateMainData(firstRun = true)
    }
}

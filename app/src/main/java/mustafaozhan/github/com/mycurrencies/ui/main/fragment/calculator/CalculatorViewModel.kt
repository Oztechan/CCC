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
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
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

    companion object {
        private const val DATE_FORMAT = "HH:mm:ss MM.dd.yyyy"
        private const val MINIMUM_ACTIVE_CURRENCY = 2
        private const val MAXIMUM_INPUT = 15
    }

    val currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()
    val calculatorViewStateLiveData: MutableLiveData<CalculatorViewState> = MutableLiveData()
    val outputLiveData: MutableLiveData<String> = MutableLiveData("0.0")
    var rates: Rates? = null

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun refreshData() {
        calculatorViewStateLiveData.postValue(CalculatorViewState.Loading)
        rates = null
        currencyListLiveData.value?.clear()

        if (getMainData().firstRun) {
            currencyDao.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }

        currencyListLiveData.postValue(currencyDao.getActiveCurrencies().removeUnUsedCurrencies())
    }

    fun getCurrencies() {
        calculatorViewStateLiveData.postValue(CalculatorViewState.Loading)
        rates?.let { rates ->
            currencyListLiveData.value?.forEach { currency ->
                currency.rate = calculateResultByCurrency(currency.name, rates)
            }
            calculatorViewStateLiveData.postValue(CalculatorViewState.Success(rates))
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
        rates?.date = DateTimeFormat.forPattern(DATE_FORMAT).print(DateTime.now())
        rates?.let {
            calculatorViewStateLiveData.postValue(CalculatorViewState.Success(it))
            offlineRatesDao.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        Crashlytics.logException(t)
        Crashlytics.log(Log.WARN, "rateDownloadFail", t.message)
        offlineRatesDao.getOfflineRatesOnBase(getMainData().currentBase.toString())?.let { offlineRates ->
            calculatorViewStateLiveData.postValue(CalculatorViewState.OfflineSuccess(offlineRates))
        } ?: run {
            calculatorViewStateLiveData.postValue(CalculatorViewState.Error)
        }
    }

    fun calculateOutput(input: String) {
        val output = Expression(
            input.replaceUnsupportedCharacters()
                .replace("%", "/100*")
        ).calculate().let {
            if (it.isNaN()) "" else it.getFormatted()
        }

        if (output.length > MAXIMUM_INPUT) {
            calculatorViewStateLiveData.postValue(CalculatorViewState.MaximumInput(input))
        } else {
            outputLiveData.postValue(output)

            if (currencyListLiveData.value?.size ?: 0 < MINIMUM_ACTIVE_CURRENCY) {
                calculatorViewStateLiveData.postValue(CalculatorViewState.FewCurrency)
            } else {
                getCurrencies()
            }
        }
    }

    fun updateCurrentBase(currency: String?) {
        rates = null
        setCurrentBase(currency)
        getCurrencies()
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

    fun calculateResultByCurrency(name: String, rate: Rates?) =
        if (outputLiveData.value.toString().isNotEmpty()) {
            try {
                rate.calculateResult(name, outputLiveData.value.toString())
            } catch (e: NumberFormatException) {
                val numericValue =
                    outputLiveData.value.toString().replaceUnsupportedCharacters().replaceNonStandardDigits()
                Crashlytics.logException(e)
                Crashlytics.log(Log.ERROR,
                    "NumberFormatException ${outputLiveData.value.toString()} to $numericValue",
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

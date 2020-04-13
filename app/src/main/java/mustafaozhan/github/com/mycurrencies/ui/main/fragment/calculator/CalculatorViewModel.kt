package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.logmob.logWarning
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.extension.calculateResult
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.replaceUnsupportedCharacters
import mustafaozhan.github.com.mycurrencies.extension.toFormattedString
import mustafaozhan.github.com.mycurrencies.extension.toPercent
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.DataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewStateObserver
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.ReverseSpinner
import org.mariuszgromada.math.mxparser.Expression
import java.util.Date

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorViewModel(
    preferencesRepository: PreferencesRepository,
    private val backendRepository: BackendRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) : DataViewModel<CalculatorViewEffect, CalculatorViewEvent, CalculatorViewState>(
    preferencesRepository
), CalculatorViewEvent {

    companion object {
        private const val MAXIMUM_INPUT = 15
        private const val KEY_DEL = "DEL"
        private const val KEY_AC = "AC"
    }

    override val viewState = CalculatorViewState(CalculatorViewStateObserver())
    override val viewEffectLiveData: MutableLiveData<CalculatorViewEffect> = MutableLiveData()
    override fun getViewEvent() = this as CalculatorViewEvent

    var rates: Rates? = null

    init {
        initData()

        viewState.apply {
            input.value = ""

            observer.input.addSource(input) { input ->
                if (input.isEmpty()) {
                    empty.postValue(true)
                    output.postValue("")
                } else {
                    calculateOutput(input)
                    empty.postValue(false)
                }
            }
        }
    }

    private fun initData() {
        refreshData()

        if (preferencesRepository.loadResetData() && !mainData.firstRun) {

            runBlocking {
                AppDatabase.database.clearAllTables()
                preferencesRepository.updateMainData(firstRun = true)
            }
            preferencesRepository.persistResetData(false)
            refreshData()
            getCurrencies()
        } else {
            getCurrencies()
        }
    }

    private fun refreshData() {
        viewState.loading.postValue(true)
        rates = null
        viewState.currencyList.value?.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }
        submitList(currencyRepository.getActiveCurrencies().removeUnUsedCurrencies())
    }

    private fun getCalculatedList(rates: Rates): MutableList<Currency>? {
        var tempList = mutableListOf<Currency>()

        viewState.currencyList.value?.let { currencyList ->
            currencyList.forEach { it.rate = calculateResultByCurrency(it.name, rates) }
            tempList = currencyList
        }

        return tempList
    }

    private fun getCurrencies() {
        viewState.loading.postValue(true)
        rates?.let { rates ->
            viewState.currencyList.value?.forEach { currency ->
                currency.rate = calculateResultByCurrency(currency.name, rates)
            }
            submitList(getCalculatedList(rates))
        } ?: run {
            viewModelScope.launch {
                subscribeService(
                    backendRepository.getAllOnBase(mainData.currentBase),
                    ::rateDownloadSuccess,
                    ::rateDownloadFail
                )
            }
        }
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) {
        rates = currencyResponse.rates
        rates?.base = currencyResponse.base
        rates?.date = Date().toFormattedString()
        rates?.let {
            submitList(getCalculatedList(it))
            offlineRatesRepository.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        logWarning(t, "rate download failed 1s time out")

        offlineRatesRepository.getOfflineRatesByBase(mainData.currentBase.toString())?.let { offlineRates ->
            submitList(getCalculatedList(offlineRates))
            viewEffectLiveData.postValue(OfflineSuccessEffect(offlineRates.date))
        } ?: run {
            viewModelScope.launch {
                subscribeService(
                    backendRepository.getAllOnBaseLongTimeOut(mainData.currentBase),
                    ::rateDownloadSuccess,
                    ::rateDownloadFailLongTimeOut
                )
            }
        }
    }

    private fun rateDownloadFailLongTimeOut(t: Throwable) {
        logWarning(t, "rate download failed on long time out")
        viewState.empty.postValue(true)

        viewState.currencyList.value?.size
            ?.whether { it > 1 }
            ?.let { viewEffectLiveData.postValue(ErrorEffect) }
    }

    private fun calculateOutput(input: String) = Expression(input.replaceUnsupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            viewState.output.postValue(output)

            output
                .whetherNot { isEmpty() }
                ?.apply { viewState.output.postValue(replaceNonStandardDigits()) }
                ?: run { viewState.output.postValue("") }

            viewState.currencyList.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.let {
                    viewState.empty.postValue(true)
                    viewEffectLiveData.postValue(FewCurrencyEffect)
                }
                ?: run { getCurrencies() }
        } ?: run { viewEffectLiveData.postValue(MaximumInputEffect(input)) }

    private fun updateCurrentBase(currency: String?) {
        rates = null
        setCurrentBase(currency)
        getCurrencies()
    }

    private fun submitList(currencyList: MutableList<Currency>?) {
        viewState.currencyList.postValue(currencyList)
        viewState.loading.postValue(false)
        if (currencyList?.isEmpty() != false) {
            viewState.empty.postValue(false)
        }
    }

    private fun calculateResultByCurrency(
        name: String,
        rate: Rates?
    ) = viewState.output.value
        ?.whetherNot { isEmpty() }
        ?.let { output ->
            try {
                rate.calculateResult(name, output)
            } catch (e: NumberFormatException) {
                val numericValue = output.replaceUnsupportedCharacters().replaceNonStandardDigits()
                logWarning(e, "NumberFormatException $output to $numericValue")
                rate.calculateResult(name, numericValue)
            }
        } ?: run { 0.0 }

    override fun currentBaseChanged(newBase: String) {
        updateCurrentBase(newBase)
        viewState.apply {
            input.postValue(input.value)
            symbol.postValue(currencyRepository.getCurrencyByName(newBase)?.symbol ?: "")
        }
    }

    // region View Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                viewState.input.postValue("")
                viewState.output.postValue("")
            }
            KEY_DEL -> {
                viewState.input.value
                    ?.whetherNot { isEmpty() }
                    ?.apply {
                        viewState.input.postValue(substring(0, length - 1))
                    }
            }
            else -> viewState.input.postValue(if (key.isEmpty()) "" else viewState.input.value.toString() + key)
        }
    }

    override fun onItemClick(currency: Currency) {
        mainDataViewState.base.postValue(currency.name)
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        viewEffectLiveData.postValue(
            LongClickEffect("1 ${mainData.currentBase.name} = " +
                "${rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = viewEffectLiveData.postValue(ReverseSpinner)

    override fun onSpinnerItemSelected(base: String) = mainDataViewState.base.postValue(base)
    // endregion
}

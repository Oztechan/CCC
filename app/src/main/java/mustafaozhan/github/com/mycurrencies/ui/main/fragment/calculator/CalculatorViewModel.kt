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
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorData
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorState
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
) : DataViewModel<CalculatorState, CalculatorEvent, CalculatorEffect, CalculatorData>(
    preferencesRepository
), CalculatorEvent {

    companion object {
        private const val MAXIMUM_INPUT = 15
        private const val KEY_DEL = "DEL"
        private const val KEY_AC = "AC"
    }

    // region SEED
    override val state: CalculatorState
        get() = CalculatorState(CalculatorViewStateObserver())
    override val event: CalculatorEvent
        get() = this
    override val effect: MutableLiveData<CalculatorEffect>
        get() = MutableLiveData()
    override val data: CalculatorData
        get() = CalculatorData()
    // endregion

    init {
        initData()

        state.apply {
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
        state.loading.postValue(true)
        data.rates = null
        state.currencyList.value?.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }
        submitList(currencyRepository.getActiveCurrencies().removeUnUsedCurrencies())
    }

    private fun getCalculatedList(rates: Rates): MutableList<Currency>? {
        var tempList = mutableListOf<Currency>()

        state.currencyList.value?.let { currencyList ->
            currencyList.forEach { it.rate = calculateResultByCurrency(it.name, rates) }
            tempList = currencyList
        }

        return tempList
    }

    private fun getCurrencies() {
        state.loading.postValue(true)
        data.rates?.let { rates ->
            state.currencyList.value?.forEach { currency ->
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

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) = data.apply {
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
            effect.postValue(OfflineSuccessEffect(offlineRates.date))
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
        state.empty.postValue(true)

        state.currencyList.value?.size
            ?.whether { it > 1 }
            ?.let { effect.postValue(ErrorEffect) }
    }

    private fun calculateOutput(input: String) = Expression(input.replaceUnsupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            state.output.postValue(output)

            output
                .whetherNot { isEmpty() }
                ?.apply { state.output.postValue(replaceNonStandardDigits()) }
                ?: run { state.output.postValue("") }

            state.currencyList.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.let {
                    state.empty.postValue(true)
                    effect.postValue(FewCurrencyEffect)
                }
                ?: run { getCurrencies() }
        } ?: run { effect.postValue(MaximumInputEffect(input)) }

    private fun updateCurrentBase(currency: String?) {
        data.rates = null
        setCurrentBase(currency)
        getCurrencies()
    }

    private fun submitList(currencyList: MutableList<Currency>?) {
        state.currencyList.postValue(currencyList)
        state.loading.postValue(false)
        if (currencyList?.isEmpty() != false) {
            state.empty.postValue(false)
        }
    }

    private fun calculateResultByCurrency(
        name: String,
        rate: Rates?
    ) = state.output.value
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
        state.apply {
            input.postValue(input.value)
            symbol.postValue(currencyRepository.getCurrencyByName(newBase)?.symbol ?: "")
        }
    }

    // region View Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                state.input.postValue("")
                state.output.postValue("")
            }
            KEY_DEL -> {
                state.input.value
                    ?.whetherNot { isEmpty() }
                    ?.apply {
                        state.input.postValue(substring(0, length - 1))
                    }
            }
            else -> state.input.postValue(if (key.isEmpty()) "" else state.input.value.toString() + key)
        }
    }

    override fun onItemClick(currency: Currency) {
        mainDataViewState.base.postValue(currency.name)
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        effect.postValue(
            LongClickEffect("1 ${mainData.currentBase.name} = " +
                "${data.rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = effect.postValue(ReverseSpinner)

    override fun onSpinnerItemSelected(base: String) = mainDataViewState.base.postValue(base)
    // endregion
}

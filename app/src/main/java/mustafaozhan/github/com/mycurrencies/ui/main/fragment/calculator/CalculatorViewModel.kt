package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.logmob.logWarning
import com.github.mustafaozhan.scopemob.either
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
import mustafaozhan.github.com.mycurrencies.extension.dropDecimal
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.replaceUnsupportedCharacters
import mustafaozhan.github.com.mycurrencies.extension.toFormattedString
import mustafaozhan.github.com.mycurrencies.extension.toPercent
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorViewState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.EmptyState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LoadingState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.OfflineSuccessEffect
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.SuccessState
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.SwitchBaseEffect
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
) : MainDataViewModel<CalculatorViewEffect, CalculatorViewEvent, CalculatorViewState>(
    preferencesRepository
), CalculatorViewEvent {

    companion object {
        private const val MAXIMUM_INPUT = 15
    }

    override val viewStateLiveData: MutableLiveData<CalculatorViewState> = MutableLiveData(EmptyState)
    override val viewEffectLiveData: MutableLiveData<CalculatorViewEffect> = MutableLiveData()

    val inputLiveData: MutableLiveData<String> = MutableLiveData("")

    val currencyListLiveData: MutableLiveData<MutableList<Currency>> = MutableLiveData()
    val outputLiveData: MutableLiveData<String> = MutableLiveData("")
    var rates: Rates? = null

    init {
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
        viewStateLiveData.postValue(LoadingState)
        rates = null
        currencyListLiveData.value?.clear()

        if (mainData.firstRun) {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }
        currencyListLiveData.postValue(currencyRepository.getActiveCurrencies().removeUnUsedCurrencies())
    }

    private fun getCalculatedList(rates: Rates): MutableList<Currency>? {
        var tempList = mutableListOf<Currency>()

        currencyListLiveData.value?.let { currencyList ->
            currencyList.forEach { it.rate = calculateResultByCurrency(it.name, rates) }
            tempList = currencyList
        }

        return tempList
    }

    private fun getCurrencies() {
        viewStateLiveData.postValue(LoadingState)
        rates?.let { rates ->
            currencyListLiveData.value?.forEach { currency ->
                currency.rate = calculateResultByCurrency(currency.name, rates)
            }
            viewStateLiveData.postValue(SuccessState(getCalculatedList(rates)))
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
            viewStateLiveData.postValue(SuccessState(getCalculatedList(it)))
            offlineRatesRepository.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        logWarning(t, "rate download failed 1s time out")

        offlineRatesRepository.getOfflineRatesByBase(mainData.currentBase.toString())?.let { offlineRates ->
            viewStateLiveData.postValue(SuccessState(getCalculatedList(offlineRates)))
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
        viewStateLiveData.postValue(EmptyState)
        viewEffectLiveData.postValue(ErrorEffect)
    }

    fun calculateOutput(input: String) = Expression(input.replaceUnsupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            outputLiveData.postValue(output)
            currencyListLiveData.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.let {
                    viewStateLiveData.postValue(EmptyState)
                    viewEffectLiveData.postValue(FewCurrencyEffect)
                }
                ?: run { getCurrencies() }
        } ?: run { viewEffectLiveData.postValue(MaximumInputEffect(input)) }

    fun updateCurrentBase(currency: String?) {
        rates = null
        setCurrentBase(currency)
        getCurrencies()
    }

    private fun getClickedItemRate(name: String): String =
        "1 ${mainData.currentBase.name} = ${rates?.getThroughReflection<Double>(name)}"

    fun getCurrencyByName(name: String) = currencyRepository.getCurrencyByName(name)

    fun verifyCurrentBase(spinnerList: List<String>): Currencies {
        mainData.currentBase
            .either(
                { equals(Currencies.NULL) },
                { spinnerList.indexOf(it.toString()) == -1 }
            )
            ?.let { updateCurrentBase(currencyListLiveData.value?.firstOrNull { it.isActive == 1 }?.name) }

        return mainData.currentBase
    }

    private fun calculateResultByCurrency(
        name: String,
        rate: Rates?
    ) = outputLiveData.value
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

    fun postEmptyState() = viewStateLiveData.postValue(EmptyState)

    override fun keyPressed(key: String) {
        inputLiveData.postValue(if (key.isEmpty()) "" else inputLiveData.value.toString() + key)
    }

    override fun delPressed() {
        inputLiveData.value
            ?.whetherNot { isEmpty() }
            ?.apply {
                inputLiveData.postValue(substring(0, length - 1))
            }
    }

    override fun acPressed() {
        inputLiveData.postValue("")
        outputLiveData.postValue("")
    }

    override fun onRowClick(currency: Currency) {
        viewEffectLiveData.postValue(SwitchBaseEffect(
            currency.rate.getFormatted().replaceNonStandardDigits().dropDecimal(),
            currency.name,
            currencyListLiveData.value?.indexOf(currency) ?: -1
        ))
    }

    override fun onRowLongClick(currency: Currency): Boolean {
        viewEffectLiveData.postValue(
            LongClickEffect("${getClickedItemRate(currency.name)} ${currency.getVariablesOneLine()}", currency.name)
        )
        return true
    }

    override fun getViewEvent() = this as CalculatorViewEvent
}

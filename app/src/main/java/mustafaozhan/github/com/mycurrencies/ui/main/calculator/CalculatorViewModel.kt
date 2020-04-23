package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.viewmodel.EASYViewModel
import com.github.mustafaozhan.logmob.logWarning
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.extension.calculateResult
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.extension.toFormattedString
import mustafaozhan.github.com.mycurrencies.extension.toPercent
import mustafaozhan.github.com.mycurrencies.extension.toSupportedCharacters
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainYield.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorAction
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorState
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorStateBacking
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorYield
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorYield.Companion.CHAR_DOT
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorYield.Companion.KEY_AC
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorYield.Companion.KEY_DEL
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorYield.Companion.MAXIMUM_INPUT
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ErrorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.FewCurrencyEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.MaximumInputEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.OfflineSuccessEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ReverseSpinner
import org.mariuszgromada.math.mxparser.Expression
import java.util.Date

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Suppress("TooManyFunctions")
class CalculatorViewModel(
    val preferencesRepository: PreferencesRepository,
    private val backendRepository: BackendRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) : EASYViewModel<CalculatorState, CalculatorAction, CalculatorEvent, CalculatorYield>(), CalculatorAction {

    // region take it EASY!
    private val _states = CalculatorStateBacking()
    override val states = CalculatorState(_states)

    private val _events = MutableLiveData<CalculatorEvent>()
    override val events: LiveData<CalculatorEvent> = _events

    override fun getActions() = this as CalculatorAction
    override val yields = CalculatorYield()
    // endregion

    init {
        initData()

        _states.apply {
            _loading.value = true
            _base.value = preferencesRepository.currentBase
            _input.value = ""

            _base.addSource(states.base) {
                currentBaseChanged(it)
            }
            _input.addSource(states.input) { input ->
                _loading.value = true
                calculateOutput(input)
            }
            _currencyList.addSource(currencyRepository.getActiveCurrencies()) {
                _currencyList.value = it.removeUnUsedCurrencies()
            }
        }
    }

    private fun initData() = viewModelScope
        .whether { preferencesRepository.firstRun }
        ?.launch {
            currencyRepository.insertInitialCurrencies()
            preferencesRepository.updateMainData(firstRun = false)
        }.run { getCurrencies() }

    private fun getCurrencies() = yields.rates?.let { rates ->
        calculateConversions(rates)
    } ?: viewModelScope.launch {
        subscribeService(
            backendRepository.getAllOnBase(preferencesRepository.currentBase),
            ::rateDownloadSuccess,
            ::rateDownloadFail
        )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse): Unit = with(yields) {
        rates = currencyResponse.rates
        rates?.base = currencyResponse.base
        rates?.date = Date().toFormattedString()
        rates?.let {
            calculateConversions(it)
            offlineRatesRepository.insertOfflineRates(it)
        }
    }

    private fun rateDownloadFail(t: Throwable) {
        logWarning(t, "rate download failed 1s time out")

        offlineRatesRepository.getOfflineRatesByBase(
            preferencesRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _events.postValue(OfflineSuccessEvent(offlineRates.date))
        } ?: viewModelScope.launch {
            subscribeService(
                backendRepository.getAllOnBaseLongTimeOut(preferencesRepository.currentBase),
                ::rateDownloadSuccess,
                ::rateDownloadFailLongTimeOut
            )
        }
    }

    private fun rateDownloadFailLongTimeOut(t: Throwable) {
        logWarning(t, "rate download failed on long time out")
        states.currencyList.value?.size
            ?.whether { it > 1 }
            ?.let { _events.postValue(ErrorEvent) }
    }

    private fun calculateOutput(input: String) = Expression(input.toSupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            _states._output.value = output

            states.currencyList.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.whetherNot { states.input.value.isNullOrEmpty() }
                ?.let { _events.setValue(FewCurrencyEvent) }
                ?: run { getCurrencies() }
        } ?: run {
        _events.postValue(MaximumInputEvent)
        _states._input.value = input.dropLast(1)
        _states._loading.value = false
    }

    private fun calculateConversions(rates: Rates?) = with(_states) {
        _currencyList.value = _currencyList.value?.onEach {
            it.rate = rates.calculateResult(it.name, _output.value)
        }
        _loading.value = false
    }

    private fun currentBaseChanged(newBase: String) {
        yields.rates = null
        preferencesRepository.currentBase = newBase

        _states._input.value = _states._input.value
        _states._symbol.value = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""

        getCurrencies()
    }

    fun verifyCurrentBase() {
        _states._base.value = preferencesRepository.currentBase
    }

    // region Actions
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                _states._input.value = ""
                _states._output.value = ""
            }
            KEY_DEL -> states.input.value
                ?.whetherNot { isEmpty() }
                ?.apply {
                    _states._input.value = substring(0, length - 1)
                }
            else -> _states._input.value = if (key.isEmpty()) "" else states.input.value.toString() + key
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) = with(_states) {
        var finalResult = conversion

        while (finalResult.length > MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _base.value = currency.name
        _input.value = finalResult
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        _events.postValue(
            LongClickEvent("1 ${preferencesRepository.currentBase} = " +
                "${yields.rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = _events.postValue(ReverseSpinner)

    override fun onSpinnerItemSelected(base: String) {
        _states._base.value = base
    }
    // endregion
}

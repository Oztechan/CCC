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

    // region SEED
    private val _state = CalculatorStateBacking()
    override val state = CalculatorState(_state)

    private val _event = MutableLiveData<CalculatorEvent>()
    override val event: LiveData<CalculatorEvent> = _event

    override val action = this as CalculatorAction
    override val yield = CalculatorYield()
    // endregion

    init {
        initData()

        _state.apply {
            _loading.value = true
            _base.value = preferencesRepository.currentBase
            _input.value = ""

            _base.addSource(state.base) {
                currentBaseChanged(it)
            }
            _input.addSource(state.input) { input ->
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

    private fun getCurrencies() = yield.rates?.let { rates ->
        calculateConversions(rates)
    } ?: viewModelScope.launch {
        subscribeService(
            backendRepository.getAllOnBase(preferencesRepository.currentBase),
            ::rateDownloadSuccess,
            ::rateDownloadFail
        )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse): Unit = with(yield) {
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
            _event.postValue(OfflineSuccessEvent(offlineRates.date))
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
        state.currencyList.value?.size
            ?.whether { it > 1 }
            ?.let { _event.postValue(ErrorEvent) }
    }

    private fun calculateOutput(input: String) = Expression(input.toSupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            _state._output.value = output

            state.currencyList.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.whetherNot { state.input.value.isNullOrEmpty() }
                ?.let { _event.postValue(FewCurrencyEvent) }
                ?: run { getCurrencies() }
        } ?: run {
        _event.postValue(MaximumInputEvent)
        _state._input.value = input.dropLast(1)
        _state._loading.value = false
    }

    private fun calculateConversions(rates: Rates?) = with(_state) {
        _currencyList.value = _currencyList.value?.onEach {
            it.rate = rates.calculateResult(it.name, _output.value)
        }
        _loading.value = false
    }

    private fun currentBaseChanged(newBase: String) {
        yield.rates = null
        preferencesRepository.currentBase = newBase

        _state.apply {
            _input.value = _input.value
            _symbol.value = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""
        }

        getCurrencies()
    }

    fun verifyCurrentBase() {
        _state._base.value = preferencesRepository.currentBase
    }

    // region View Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                _state._input.value = ""
                _state._output.value = ""
            }
            KEY_DEL -> state.input.value
                ?.whetherNot { isEmpty() }
                ?.apply {
                    _state._input.value = substring(0, length - 1)
                }
            else -> _state._input.value = if (key.isEmpty()) "" else state.input.value.toString() + key
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) = with(_state) {
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
        _event.postValue(
            LongClickEvent("1 ${preferencesRepository.currentBase} = " +
                "${yield.rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = _event.postValue(ReverseSpinner)

    override fun onSpinnerItemSelected(base: String) {
        _state._base.value = base
    }
    // endregion
}

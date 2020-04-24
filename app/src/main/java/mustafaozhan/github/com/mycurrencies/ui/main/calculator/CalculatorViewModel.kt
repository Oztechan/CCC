package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.lifecycle.MutableSingleLiveData
import com.github.mustafaozhan.basemob.lifecycle.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.SEEDViewModel
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
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.CHAR_DOT
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.KEY_AC
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.KEY_DEL
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.MAXIMUM_INPUT
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorState
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorStateBacking
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ErrorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.FewCurrencyEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.MaximumInputEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.OfflineSuccessEffect
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
) : SEEDViewModel<CalculatorState, CalculatorEvent, CalculatorEffect, CalculatorData>(), CalculatorEvent {

    // region SEED
    private val _state = CalculatorStateBacking()
    override val state = CalculatorState(_state)

    private val _effect = MutableSingleLiveData<CalculatorEffect>()
    override val effect: SingleLiveData<CalculatorEffect> = _effect

    override val data = CalculatorData()

    override fun getEvent() = this as CalculatorEvent
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

    private fun getCurrencies() = data.rates?.let { rates ->
        calculateConversions(rates)
    } ?: viewModelScope.launch {
        subscribeService(
            backendRepository.getAllOnBase(preferencesRepository.currentBase),
            ::rateDownloadSuccess,
            ::rateDownloadFail
        )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse): Unit = with(data) {
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
            _effect.value = OfflineSuccessEffect(offlineRates.date)
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
            ?.let { _effect.value = ErrorEffect }
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
                ?.let { _effect.value = FewCurrencyEffect }
                ?: run { getCurrencies() }
        } ?: run {
        _effect.value = MaximumInputEffect
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
        data.rates = null
        preferencesRepository.currentBase = newBase

        _state._input.value = _state._input.value
        _state._symbol.value = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""

        getCurrencies()
    }

    fun verifyCurrentBase() {
        _state._base.value = preferencesRepository.currentBase
    }

    // region Event
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
        _effect.value = LongClickEffect("1 ${preferencesRepository.currentBase} = " +
            "${data.rates?.getThroughReflection<Double>(currency.name)} " +
            currency.getVariablesOneLine(),
            currency.name
        )
        return true
    }

    override fun onBarClick() {
        _effect.value = ReverseSpinner
    }

    override fun onSpinnerItemSelected(base: String) {
        _state._base.value = base
    }
    // endregion
}

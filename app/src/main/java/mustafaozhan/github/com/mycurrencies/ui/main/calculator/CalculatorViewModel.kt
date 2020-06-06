/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mustafaozhan.github.com.mycurrencies.data.api.ApiRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainData.Companion.MINIMUM_ACTIVE_CURRENCY
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.CHAR_DOT
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.KEY_AC
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.KEY_DEL
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.MAXIMUM_INPUT
import mustafaozhan.github.com.mycurrencies.util.extension.calculateResult
import mustafaozhan.github.com.mycurrencies.util.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.util.extension.getThroughReflection
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.util.extension.toPercent
import mustafaozhan.github.com.mycurrencies.util.extension.toRate
import mustafaozhan.github.com.mycurrencies.util.extension.toSupportedCharacters
import org.mariuszgromada.math.mxparser.Expression
import timber.log.Timber

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    val preferencesRepository: PreferencesRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseViewModel(), CalculatorEvent {

    // region SEED
    private val _state = CalculatorStateBacking()
    val state = CalculatorState(_state)

    private val _effect = MutableSingleLiveData<CalculatorEffect>()
    val effect: SingleLiveData<CalculatorEffect> = _effect

    val data = CalculatorData()

    fun getEvent() = this as CalculatorEvent
    // endregion

    init {
        with(_state) {
            _base.value = preferencesRepository.currentBase
            _input.value = ""

            _base.addSource(state.base) {
                currentBaseChanged(it)
            }
            _input.addSource(state.input) { input ->
                _loading.value = true
                calculateOutput(input)
            }

            viewModelScope.launch {
                currencyDao.getActiveCurrencies()
                    .map { it.removeUnUsedCurrencies() }
                    .collect { _currencyList.value = it }
            }
        }
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
    } ?: viewModelScope.launch {
        apiRepository
            .getRatesByBase(preferencesRepository.currentBase)
            .execute(
                ::rateDownloadSuccess,
                ::rateDownloadFail
            )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) = viewModelScope.launch {
        currencyResponse.toRate().let {
            data.rates = it
            calculateConversions(it)
            offlineRatesDao.insertOfflineRates(it)
        }
    }.toUnit()

    private fun rateDownloadFail(t: Throwable) = viewModelScope.launch {
        Timber.w(t, "rate download failed.")

        offlineRatesDao.getOfflineRatesByBase(
            preferencesRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _effect.postValue(OfflineSuccessEffect(offlineRates.date))
        } ?: run {
            Timber.w(t, "no offline rate found")
            state.currencyList.value?.size
                ?.whether { it > 1 }
                ?.let { _effect.postValue(ErrorEffect) }
        }
    }.toUnit()

    private fun calculateOutput(input: String) = Expression(input.toSupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        .whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            _state._output.value = output
            state.currencyList.value?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.whetherNot { state.input.value.isNullOrEmpty() }
                ?.let { _effect.postValue(FewCurrencyEffect) }
                ?: run { getRates() }
        } ?: run {
        _effect.postValue(MaximumInputEffect)
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

        viewModelScope.launch {
            _state._symbol.value = currencyDao.getCurrencyByName(newBase)?.symbol ?: ""
        }
    }

    fun verifyCurrentBase(it: String) {
        _state._base.postValue(it)
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
        _effect.postValue(
            ShowRateEffect("1 ${preferencesRepository.currentBase} = " +
                "${data.rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = _effect.postValue(OpenBarEffect)

    override fun onSpinnerItemSelected(base: String) {
        _state._base.value = base
    }
    // endregion
}

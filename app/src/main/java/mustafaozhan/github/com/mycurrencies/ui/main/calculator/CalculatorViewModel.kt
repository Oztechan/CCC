package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorState
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorStateMediator
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

    companion object {
        private const val MINIMUM_ACTIVE_CURRENCY = 2
        private const val MAXIMUM_INPUT = 15
        private const val KEY_DEL = "DEL"
        private const val KEY_AC = "AC"
    }

    override val state = CalculatorState(CalculatorStateMediator())
    override val event = this as CalculatorEvent
    override val effect = MutableLiveData<CalculatorEffect>()
    override val data = CalculatorData()

    init {
        initData()

        state.apply {
            loading.value = true
            base.value = preferencesRepository.currentBase
            input.value = ""

            mediator.base.addSource(base) {
                currentBaseChanged(it)
            }
            mediator.input.addSource(input) { input ->
                loading.value = true
                calculateOutput(input)
            }
            mediator.currencyList.addSource(currencyRepository.getActiveCurrencies()) {
                currencyList.value = it.removeUnUsedCurrencies()
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
            effect.postValue(OfflineSuccessEffect(offlineRates.date))
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
            ?.let { effect.postValue(ErrorEffect) }
    }

    private fun calculateOutput(input: String) = Expression(input.toSupportedCharacters().toPercent())
        .calculate()
        .mapTo { if (isNaN()) "" else getFormatted() }
        ?.whether { length <= MAXIMUM_INPUT }
        ?.let { output ->
            state.output.value = output

            state.currencyList.value
                ?.size
                ?.whether { it < MINIMUM_ACTIVE_CURRENCY }
                ?.whetherNot { state.input.value.isNullOrEmpty() }
                ?.let { effect.postValue(FewCurrencyEffect) }
                ?: run { getCurrencies() }
        } ?: run {
        effect.postValue(MaximumInputEffect)
        state.input.value = input.dropLast(1)
        state.loading.value = false
    }

    private fun calculateConversions(rates: Rates?) = with(state) {
        currencyList.value = currencyList.value?.onEach {
            it.rate = rates.calculateResult(it.name, output.value)
        }
        loading.value = false
    }

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        preferencesRepository.currentBase = newBase

        state.apply {
            input.value = input.value
            symbol.value = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""
        }

        getCurrencies()
    }

    fun verifyCurrentBase() {
        state.base.value = preferencesRepository.currentBase
    }

    // region View Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                state.input.value = ""
                state.output.value = ""
            }
            KEY_DEL -> state.input.value
                ?.whetherNot { isEmpty() }
                ?.apply {
                    state.input.value = substring(0, length - 1)
                }
            else -> state.input.value = if (key.isEmpty()) "" else state.input.value.toString() + key
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) = with(state) {
        base.value = currency.name
        input.value = conversion
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        effect.postValue(
            LongClickEffect("1 ${preferencesRepository.currentBase} = " +
                "${data.rates?.getThroughReflection<Double>(currency.name)} " +
                currency.getVariablesOneLine(),
                currency.name
            )
        )
        return true
    }

    override fun onBarClick() = effect.postValue(ReverseSpinner)

    override fun onSpinnerItemSelected(base: String) {
        state.base.value = base
    }
    // endregion
}

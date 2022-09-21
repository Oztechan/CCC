package com.oztechan.ccc.client.viewmodel.calculator

import com.github.submob.parsermob.ParserMob
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.model.RateState
import com.oztechan.ccc.common.model.Rates
import kotlinx.coroutines.flow.MutableStateFlow

// State
data class CalculatorState(
    val input: String = "",
    val base: String = "",
    val currencyList: List<Currency> = listOf(),
    val output: String = "",
    val symbol: String = "",
    val loading: Boolean = true,
    val rateState: RateState = RateState.None,
) : BaseState()

// Event
interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency)
    fun onItemImageLongClick(currency: Currency)
    fun onItemAmountLongClick(amount: String)
    fun onBarClick()
    fun onSettingsClicked()
    fun onBaseChange(base: String)
}

// Effect
sealed class CalculatorEffect : BaseEffect() {
    object Error : CalculatorEffect()
    object FewCurrency : CalculatorEffect()
    object OpenBar : CalculatorEffect()
    object MaximumInput : CalculatorEffect()
    object OpenSettings : CalculatorEffect()
    data class CopyToClipboard(val amount: String) : CalculatorEffect()
    data class ShowRate(val text: String, val name: String) : CalculatorEffect()
}

// Data
data class CalculatorData(
    var parser: ParserMob = ParserMob(),
    var rates: Rates? = null
) : BaseData() {
    companion object {
        internal const val MAXIMUM_OUTPUT = 18
        internal const val MAXIMUM_INPUT = 44
        internal const val CHAR_DOT = '.'
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
    }
}

// Extension
@Suppress("LongParameterList")
internal fun MutableStateFlow<CalculatorState>.update(
    input: String = value.input,
    base: String = value.base,
    currencyList: List<Currency> = value.currencyList,
    output: String = value.output,
    symbol: String = value.symbol,
    loading: Boolean = value.loading,
    rateState: RateState = value.rateState
) {
    value = value.copy(
        input = input,
        base = base,
        currencyList = currencyList,
        output = output,
        symbol = symbol,
        loading = loading,
        rateState = rateState
    )
}

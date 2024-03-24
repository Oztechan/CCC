package com.oztechan.ccc.client.viewmodel.calculator

import com.github.submob.parsermob.ParserMob
import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.client.viewmodel.calculator.model.ConversionState
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency

// State
data class CalculatorState(
    val isBannerAdVisible: Boolean,
    val input: String = "",
    val base: String = "",
    val currencyList: List<Currency> = listOf(),
    val output: String = "",
    val symbol: String = "",
    val loading: Boolean = true,
    val conversionState: ConversionState = ConversionState.None,
) : BaseState

// Event
interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency)
    fun onItemImageLongClick(currency: Currency)
    fun onItemAmountLongClick(amount: String)
    fun onOutputLongClick()
    fun onInputLongClick()
    fun onPasteToInput(text: String)
    fun onBarClick()
    fun onSettingsClicked()
    fun onBaseChange(base: String)
}

// Effect
sealed class CalculatorEffect : BaseEffect {
    data object Error : CalculatorEffect()
    data object FewCurrency : CalculatorEffect()
    data object OpenBar : CalculatorEffect()
    data object TooBigInput : CalculatorEffect()
    data object TooBigOutput : CalculatorEffect()
    data object OpenSettings : CalculatorEffect()
    data object ShowPasteRequest : CalculatorEffect()
    data class CopyToClipboard(val amount: String) : CalculatorEffect()
    data class ShowConversion(val text: String, val code: String) : CalculatorEffect()
}

// Data
data class CalculatorData(
    var parser: ParserMob = ParserMob(),
    var conversion: Conversion? = null
) : BaseData {
    companion object {
        internal const val MAXIMUM_OUTPUT = 18
        internal const val MAXIMUM_INPUT = 44
        internal const val CHAR_DOT = '.'
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
    }
}

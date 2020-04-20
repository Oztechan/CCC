package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData

data class CalculatorData(
    var rates: Rates? = null
) : MainActivityData() {
    companion object {
        const val MAXIMUM_INPUT = 15
        const val KEY_DEL = "DEL"
        const val KEY_AC = "AC"
        const val CHAR_DOT = '.'
    }
}

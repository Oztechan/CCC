package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainYield

data class CalculatorYield(
    var rates: Rates? = null
) : MainYield() {
    companion object {
        const val MAXIMUM_INPUT = 15
        const val KEY_DEL = "DEL"
        const val KEY_AC = "AC"
        const val CHAR_DOT = '.'
    }
}

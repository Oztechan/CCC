/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData

data class CalculatorData(
    var rates: Rates? = null
) : MainActivityData() {
    companion object {
        internal const val MAXIMUM_INPUT = 15
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
        internal const val CHAR_DOT = '.'
    }
}

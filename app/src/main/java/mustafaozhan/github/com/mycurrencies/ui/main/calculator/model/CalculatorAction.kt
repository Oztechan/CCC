package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import com.github.mustafaozhan.basemob.model.BaseAction
import mustafaozhan.github.com.mycurrencies.model.Currency

interface CalculatorAction : BaseAction {

    fun onKeyPress(key: String)

    fun onItemClick(currency: Currency, conversion: String)

    fun onItemLongClick(currency: Currency): Boolean

    fun onBarClick()

    fun onSpinnerItemSelected(base: String)
}

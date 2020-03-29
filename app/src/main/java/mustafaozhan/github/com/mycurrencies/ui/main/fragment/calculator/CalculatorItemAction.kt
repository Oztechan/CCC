package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import com.github.mustafaozhan.basemob.action.BaseItemAction
import mustafaozhan.github.com.mycurrencies.model.Currency

interface CalculatorItemAction : BaseItemAction {

    fun switchBase(amount: String, currency: Currency)

    fun showCurrencyComparison(currency: Currency)
}

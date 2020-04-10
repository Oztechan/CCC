package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.view.BaseViewEvent
import mustafaozhan.github.com.mycurrencies.model.Currency

interface CalculatorViewEvent : BaseViewEvent {

    fun onRowClick(currency: Currency)

    fun onRowLongClick(currency: Currency): Boolean
}

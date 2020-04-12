package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.view.BaseViewEvent
import mustafaozhan.github.com.mycurrencies.model.Currency

interface CalculatorViewEvent : BaseViewEvent {

    fun onKeyPressed(key: String)

    fun onDelPressed()

    fun onAcPressed()

    fun onItemClick(currency: Currency)

    fun onItemLongClick(currency: Currency): Boolean
}

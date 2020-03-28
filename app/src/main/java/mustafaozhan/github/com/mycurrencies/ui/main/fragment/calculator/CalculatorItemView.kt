package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import com.github.mustafaozhan.basemob.view.BaseItemView
import mustafaozhan.github.com.mycurrencies.databinding.ItemCurrencyBinding
import mustafaozhan.github.com.mycurrencies.model.Currency

interface CalculatorItemView : BaseItemView {
    fun onCalculatorItemClick(itemCurrencyBinding: ItemCurrencyBinding, currency: Currency)
    fun onCalculatorItemLongClick(itemCurrencyBinding: ItemCurrencyBinding, currency: Currency)
}

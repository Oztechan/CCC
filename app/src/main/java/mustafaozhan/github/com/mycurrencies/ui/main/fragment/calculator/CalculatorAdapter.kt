package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.view.View
import android.view.ViewGroup
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.databinding.ItemCurrencyBinding
import mustafaozhan.github.com.mycurrencies.function.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.function.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.function.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CalculatorAdapter : BaseRecyclerViewAdapter<Currency, ItemCurrencyBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency, ItemCurrencyBinding> =
        RatesViewHolder(getViewHolderView(parent, R.layout.item_currency))

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency, ItemCurrencyBinding>(itemView) {

        override fun bind(item: Currency) {
            with(binding) {
                txtType.text = item.name
                txtSymbol.text = item.symbol
                txtAmount.text = item.rate.getFormatted().replaceNonStandardDigits()
                imgItem.setBackgroundByName(item.name)
            }
        }
    }
}

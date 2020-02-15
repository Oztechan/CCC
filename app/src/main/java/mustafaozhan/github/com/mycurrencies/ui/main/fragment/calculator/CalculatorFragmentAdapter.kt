package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_currency.view.img_item
import kotlinx.android.synthetic.main.item_currency.view.txt_amount
import kotlinx.android.synthetic.main.item_currency.view.txt_symbol
import kotlinx.android.synthetic.main.item_currency.view.txt_type
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CalculatorFragmentAdapter : BaseRecyclerViewAdapter<Currency>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency> =
        RatesViewHolder(getViewHolderView(parent, R.layout.item_currency))

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency>(itemView) {

        override fun bind(item: Currency) {
            itemView.apply {
                txt_type.text = item.name
                txt_symbol.text = item.symbol
                txt_amount.text = item.rate.getFormatted().replaceNonStandardDigits()
                img_item.setBackgroundByName(item.name)
            }
        }
    }
}

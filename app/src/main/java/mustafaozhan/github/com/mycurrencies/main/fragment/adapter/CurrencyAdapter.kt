package mustafaozhan.github.com.mycurrencies.main.fragment.adapter

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_currency.view.imgRow
import kotlinx.android.synthetic.main.item_currency.view.txtAmount
import kotlinx.android.synthetic.main.item_currency.view.txtSymbol
import kotlinx.android.synthetic.main.item_currency.view.txtType
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.extensions.getFormatted
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.room.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CurrencyAdapter : BaseRecyclerViewAdapter<Currency>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency> =
        RatesViewHolder(getViewHolderView(parent, R.layout.item_currency))

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency>(itemView) {

        override fun bind(item: Currency) {
            itemView.apply {
                txtType.text = item.name
                txtSymbol.text = item.symbol
                txtAmount.text = item.rate.getFormatted()
                imgRow.setBackgroundByName(item.name)
            }
        }
    }
}
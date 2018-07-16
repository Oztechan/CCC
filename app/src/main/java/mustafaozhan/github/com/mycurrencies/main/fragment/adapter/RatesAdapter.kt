package mustafaozhan.github.com.mycurrencies.main.fragment.adapter

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_rates.view.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.setBackgroundByName

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class RatesAdapter : BaseRecyclerViewAdapter<Currency>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency> =
            RatesViewHolder(getViewHolderView(parent, R.layout.item_rates))

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency>(itemView) {
        override fun bind(item: Currency) {
            itemView.txtType.text = item.name
            itemView.txtAmount.text = item.rate.toString()
            itemView.imgBase.setBackgroundByName(item.name)
        }
    }

}
package mustafaozhan.github.com.mycurrencies.settings.adapter

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_setting.view.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.room.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingAdapter : BaseRecyclerViewAdapter<Currency>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency> =
            RatesViewHolder(getViewHolderView(parent, R.layout.item_setting))

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency>(itemView) {
        override fun bind(item: Currency) {
            itemView.textView.text = item.name
            itemView.checkBox.isChecked = item.isActive == 1
            itemView.icon.setBackgroundByName(item.name)

        }
    }
}
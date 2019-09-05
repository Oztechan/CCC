package mustafaozhan.github.com.mycurrencies.settings.adapter

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.item_setting.view.checkBox
import kotlinx.android.synthetic.main.item_setting.view.img_icon
import kotlinx.android.synthetic.main.item_setting.view.txt_setting_item
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.extensions.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingAdapter : BaseRecyclerViewAdapter<Currency>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency> =
        RatesViewHolder(getViewHolderView(parent, R.layout.item_setting))

    override fun onBindViewHolder(holder: BaseViewHolder<Currency>, position: Int) {
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.fall_down
        ))
        super.onBindViewHolder(holder, position)
    }

    class RatesViewHolder(itemView: View) : BaseViewHolder<Currency>(itemView) {
        override fun bind(item: Currency) {
            itemView.apply {
                txt_setting_item.text = item.getVariablesOneLine()
                checkBox.isChecked = item.isActive == 1
                img_icon.setBackgroundByName(item.name)
            }
        }
    }
}

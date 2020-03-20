package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.github.mustafaozhan.basemob.adapter.BaseRecyclerViewAdapter
import com.github.mustafaozhan.basemob.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingBinding
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingsAdapter : BaseRecyclerViewAdapter<Currency, ItemSettingBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesViewHolder(ItemSettingBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    override fun onBindViewHolder(holder: BaseViewHolder<Currency, ItemSettingBinding>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Currency, ItemSettingBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesViewHolder(itemBinding: ItemSettingBinding) :
        BaseViewHolder<Currency, ItemSettingBinding>(itemBinding) {

        override fun bindItem(item: Currency) {
            with(itemBinding) {
                txtSettingItem.text = item.getVariablesOneLine()
                checkBox.isChecked = item.isActive == 1
                imgIcon.setBackgroundByName(item.name)
            }
            itemView.setOnClickListener { onItemClickListener(item, itemBinding, adapterPosition) }
        }
    }
}

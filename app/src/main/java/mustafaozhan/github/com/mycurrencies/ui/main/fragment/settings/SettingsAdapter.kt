package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseVBViewHolder
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingBinding
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingsAdapter : BaseVBRecyclerViewAdapter<Currency, ItemSettingBinding>(SettingsDiffer()) {

    lateinit var onItemClickListener: ((Currency, ItemSettingBinding) -> Unit)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesViewBindingViewHolder(ItemSettingBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    override fun onBindViewHolder(holder: BaseVBViewHolder<Currency, ItemSettingBinding>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseVBViewHolder<Currency, ItemSettingBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesViewBindingViewHolder(itemBinding: ItemSettingBinding) :
        BaseVBViewHolder<Currency, ItemSettingBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            txtSettingItem.text = item.getVariablesOneLine()
            checkBox.isChecked = item.isActive == 1
            imgIcon.setBackgroundByName(item.name)

            itemView.setOnClickListener {
                onItemClickListener(item, itemBinding)
            }
        }
    }

    class SettingsDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
    }
}

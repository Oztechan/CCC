package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseVBViewHolder
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingsBinding
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEvent

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingsAdapter(
    private val viewEvent: SettingsViewEvent
) : BaseVBRecyclerViewAdapter<Currency, ItemSettingsBinding>(SettingsDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesViewBindingViewHolder(ItemSettingsBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    override fun onBindViewHolder(holder: BaseVBViewHolder<Currency, ItemSettingsBinding>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseVBViewHolder<Currency, ItemSettingsBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesViewBindingViewHolder(itemBinding: ItemSettingsBinding) :
        BaseVBViewHolder<Currency, ItemSettingsBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            txtSettingItem.text = item.getVariablesOneLine()
            checkBox.isChecked = item.isActive == 1
            imgIcon.setBackgroundByName(item.name)

            itemView.setOnClickListener {
                item.isActive = if (item.isActive == 0) 1 else 0
                viewEvent.onItemClicked(if (item.isActive == 0) 1 else 0, item.name)
                itemBinding.checkBox.isChecked = item.isActive == 0
            }
        }
    }

    class SettingsDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
    }
}

package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseDBViewHolder
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingsBinding
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view.SettingsViewEvent

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingsAdapter(
    private val settingsViewEvent: SettingsViewEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemSettingsBinding>(SettingsDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesDBViewHolder(ItemSettingsBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    override fun onBindViewHolder(holder: BaseDBViewHolder<Currency, ItemSettingsBinding>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseDBViewHolder<Currency, ItemSettingsBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesDBViewHolder(itemBinding: ItemSettingsBinding) :
        BaseDBViewHolder<Currency, ItemSettingsBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.viewEvent = settingsViewEvent
        }
    }

    class SettingsDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

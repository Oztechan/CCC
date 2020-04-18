package mustafaozhan.github.com.mycurrencies.ui.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseDBViewHolder
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingsBinding
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.settings.model.SettingsEvent

/**
 * Created by Mustafa Ozhan on 2018-07-18.
 */
class SettingsAdapter(
    private val settingsViewEvent: SettingsEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemSettingsBinding>(SettingsDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesDBViewHolder(ItemSettingsBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    inner class RatesDBViewHolder(itemBinding: ItemSettingsBinding) :
        BaseDBViewHolder<Currency, ItemSettingsBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.event = settingsViewEvent
        }
    }

    class SettingsDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

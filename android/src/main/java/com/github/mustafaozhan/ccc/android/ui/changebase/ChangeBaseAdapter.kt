package com.github.mustafaozhan.ccc.android.ui.changebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseEvent
import mustafaozhan.github.com.mycurrencies.databinding.ItemChangeBaseBinding

class ChangeBaseAdapter(
    private val changeBaseEvent: ChangeBaseEvent
) : BaseVBRecyclerViewAdapter<Currency, ItemChangeBaseBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemChangeBaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(itemBinding: ItemChangeBaseBinding) :
        BaseVBViewHolder<Currency, ItemChangeBaseBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            imgIcon.setBackgroundByName(item.name)
            txtSettingItem.text = item.getVariablesOneLine()
            root.setOnClickListener { changeBaseEvent.onItemClick(item) }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = false

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

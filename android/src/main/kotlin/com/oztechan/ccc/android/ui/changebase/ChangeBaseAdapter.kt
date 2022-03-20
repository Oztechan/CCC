package com.oztechan.ccc.android.ui.changebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.android.util.setBackgroundByName
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseEvent
import mustafaozhan.github.com.mycurrencies.databinding.ItemChangeBaseBinding

class ChangeBaseAdapter(
    private val changeBaseEvent: ChangeBaseEvent
) : BaseVBRecyclerViewAdapter<Currency>(CalculatorDiffer()) {

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

    inner class CalculatorVBViewHolder(private val itemBinding: ItemChangeBaseBinding) :
        BaseVBViewHolder<Currency>(itemBinding) {

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

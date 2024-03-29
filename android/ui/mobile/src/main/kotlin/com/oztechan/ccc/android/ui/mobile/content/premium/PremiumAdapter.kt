package com.oztechan.ccc.android.ui.mobile.content.premium

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.android.ui.mobile.databinding.ItemPremiumBinding
import com.oztechan.ccc.client.viewmodel.premium.PremiumEvent
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType

class PremiumAdapter(
    private val premiumEvent: PremiumEvent
) : BaseVBRecyclerViewAdapter<PremiumType>(PremiumDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemPremiumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(private val itemBinding: ItemPremiumBinding) :
        BaseVBViewHolder<PremiumType>(itemBinding) {

        override fun onItemBind(item: PremiumType) = with(itemBinding) {
            root.setOnClickListener { premiumEvent.onPremiumItemClick(item) }
            txtDuration.text = item.data.duration
            txtCost.text = item.data.cost
        }
    }

    class PremiumDiffer : DiffUtil.ItemCallback<PremiumType>() {
        override fun areItemsTheSame(oldItem: PremiumType, newItem: PremiumType) = false

        override fun areContentsTheSame(oldItem: PremiumType, newItem: PremiumType) = false
    }
}

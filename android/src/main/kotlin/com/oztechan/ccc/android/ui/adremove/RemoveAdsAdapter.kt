package com.oztechan.ccc.android.ui.adremove

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveEvent
import mustafaozhan.github.com.mycurrencies.databinding.ItemAdRemoveBinding

class RemoveAdsAdapter(
    private val removeAdsEvent: AdRemoveEvent
) : BaseVBRecyclerViewAdapter<RemoveAdType>(RemoveAdDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemAdRemoveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(private val itemBinding: ItemAdRemoveBinding) :
        BaseVBViewHolder<RemoveAdType>(itemBinding) {

        override fun onItemBind(item: RemoveAdType) = with(itemBinding) {
            root.setOnClickListener { removeAdsEvent.onAdRemoveItemClick(item) }
            txtReward.text = item.data.reward
            txtCost.text = item.data.cost
        }
    }

    class RemoveAdDiffer : DiffUtil.ItemCallback<RemoveAdType>() {
        override fun areItemsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false

        override fun areContentsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false
    }
}

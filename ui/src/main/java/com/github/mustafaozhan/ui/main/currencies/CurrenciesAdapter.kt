/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.base.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.ui.databinding.ItemCurrenciesBinding

class CurrenciesAdapter(
    private val currenciesEvent: CurrenciesEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemCurrenciesBinding>(CurrenciesDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesDBViewHolder(ItemCurrenciesBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    override fun onBindViewHolder(holder: BaseDBViewHolder<Currency, ItemCurrenciesBinding>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseDBViewHolder<Currency, ItemCurrenciesBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesDBViewHolder(itemBinding: ItemCurrenciesBinding) :
        BaseDBViewHolder<Currency, ItemCurrenciesBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.event = currenciesEvent
        }
    }

    class CurrenciesDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem.isActive == newItem.isActive
    }
}

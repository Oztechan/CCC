package com.oztechan.ccc.android.ui.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.databinding.ItemCurrenciesBinding
import com.oztechan.ccc.android.util.setBackgroundByName
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesEvent

class CurrenciesAdapter(
    private val currenciesEvent: CurrenciesEvent
) : BaseVBRecyclerViewAdapter<Currency>(CurrenciesDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CurrenciesVBViewHolder(
        ItemCurrenciesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BaseVBViewHolder<Currency>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )
        super.onBindViewHolder(holder, position)
    }

    override fun onViewDetachedFromWindow(holder: BaseVBViewHolder<Currency>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class CurrenciesVBViewHolder(private val itemBinding: ItemCurrenciesBinding) :
        BaseVBViewHolder<Currency>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            imgIcon.setBackgroundByName(item.code)
            txtSettingItem.text = item.getVariablesOneLine()
            checkBox.isChecked = item.isActive
            root.setOnClickListener { currenciesEvent.onItemClick(item) }
            root.setOnLongClickListener {
                currenciesEvent.onItemLongClick()
                true
            }
        }
    }

    class CurrenciesDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = true

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

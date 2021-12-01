package com.github.mustafaozhan.ccc.android.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEvent
import com.mustafaozhan.github.analytics.AnalyticsManager
import com.mustafaozhan.github.analytics.model.EventParam
import com.mustafaozhan.github.analytics.model.FirebaseEvent
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent,
    private val analyticsManager: AnalyticsManager
) : BaseVBRecyclerViewAdapter<Currency, ItemCalculatorBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemCalculatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(itemBinding: ItemCalculatorBinding) :
        BaseVBViewHolder<Currency, ItemCalculatorBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            txtAmount.text = item.rate.getFormatted().toStandardDigits()
            txtSymbol.text = item.symbol
            txtType.text = item.name
            imgItem.setBackgroundByName(item.name)
            root.setOnClickListener {
                calculatorEvent.onItemClick(item)

                analyticsManager.trackEvent(
                    FirebaseEvent.CHANGE_BASE,
                    mapOf(EventParam.NEW_BASE to item.name)
                )
            }
            root.setOnLongClickListener { calculatorEvent.onItemLongClick(item) }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

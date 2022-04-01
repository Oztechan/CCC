package com.oztechan.ccc.android.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.EventParam
import com.oztechan.ccc.analytics.model.FirebaseEvent
import com.oztechan.ccc.android.util.setBackgroundByName
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.util.getFormatted
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent,
    private val analyticsManager: AnalyticsManager
) : BaseVBRecyclerViewAdapter<Currency>(CalculatorDiffer()) {

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

    inner class CalculatorVBViewHolder(private val itemBinding: ItemCalculatorBinding) :
        BaseVBViewHolder<Currency>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            txtAmount.text = item.rate.getFormatted().toStandardDigits()
            txtSymbol.text = item.symbol
            txtType.text = item.name
            imgItem.setBackgroundByName(item.name)

            root.setOnClickListener {
                analyticsManager.trackEvent(
                    FirebaseEvent.BASE_CHANGE,
                    mapOf(EventParam.BASE to item.name)
                )

                calculatorEvent.onItemClick(item)
            }

            imgItem.setOnLongClickListener {
                analyticsManager.trackEvent(
                    FirebaseEvent.SHOW_CONVERSION,
                    mapOf(EventParam.BASE to item.name)
                )

                calculatorEvent.onItemImageLongClick(item)

                true
            }

            txtAmount.setOnLongClickListener {
                calculatorEvent.onItemAmountLongClick(txtAmount.text.toString())
                true
            }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

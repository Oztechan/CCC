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
            with(txtAmount) {
                text = item.rate.getFormatted().toStandardDigits()
                setOnLongClickListener { onOutputLongClick() }
                setOnClickListener { root.callOnClick() }
            }

            with(txtSymbol) {
                text = item.symbol
                setOnLongClickListener { onOutputLongClick() }
                setOnClickListener { root.callOnClick() }
            }

            with(txtType) {
                text = item.name
                setOnLongClickListener { onCurrencyLongClick(item) }
                setOnClickListener { root.callOnClick() }
            }

            with(imgItem) {
                setBackgroundByName(item.name)
                setOnLongClickListener { onCurrencyLongClick(item) }
                setOnClickListener { root.callOnClick() }
            }

            root.setOnClickListener {
                analyticsManager.trackEvent(
                    FirebaseEvent.BASE_CHANGE,
                    mapOf(EventParam.BASE to item.name)
                )

                calculatorEvent.onItemClick(item)
            }
        }

        private fun onOutputLongClick(): Boolean {
            calculatorEvent.onItemAmountLongClick(itemBinding.txtAmount.text.toString())
            analyticsManager.trackEvent(FirebaseEvent.COPY_CLIPBOARD)
            return true
        }

        private fun onCurrencyLongClick(item: Currency): Boolean {
            analyticsManager.trackEvent(
                FirebaseEvent.SHOW_CONVERSION,
                mapOf(EventParam.BASE to item.name)
            )

            calculatorEvent.onItemImageLongClick(item)

            return true
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

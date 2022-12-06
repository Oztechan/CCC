package com.oztechan.ccc.android.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.android.databinding.ItemCalculatorBinding
import com.oztechan.ccc.android.util.setBackgroundByName
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEvent

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent
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
                text = item.rate
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
                calculatorEvent.onItemClick(item)
            }
        }

        private fun onOutputLongClick(): Boolean {
            calculatorEvent.onItemAmountLongClick(itemBinding.txtAmount.text.toString())
            return true
        }

        private fun onCurrencyLongClick(item: Currency): Boolean {
            calculatorEvent.onItemImageLongClick(item)
            return true
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

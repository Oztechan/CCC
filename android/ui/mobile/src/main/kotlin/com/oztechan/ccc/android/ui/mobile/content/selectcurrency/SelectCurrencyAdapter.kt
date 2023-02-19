package com.oztechan.ccc.android.ui.mobile.content.selectcurrency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.submob.basemob.adapter.BaseVBRecyclerViewAdapter
import com.oztechan.ccc.android.ui.mobile.databinding.ItemSelectCurrencyBinding
import com.oztechan.ccc.android.ui.mobile.util.setBackgroundByName
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyEvent
import com.oztechan.ccc.common.core.model.Currency

class SelectCurrencyAdapter(
    private val selectCurrencyEvent: SelectCurrencyEvent
) : BaseVBRecyclerViewAdapter<Currency>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemSelectCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(private val itemBinding: ItemSelectCurrencyBinding) :
        BaseVBViewHolder<Currency>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            imgIcon.setBackgroundByName(item.code)
            txtSettingItem.text = item.getVariablesOneLine()
            root.setOnClickListener { selectCurrencyEvent.onItemClick(item) }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = false

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

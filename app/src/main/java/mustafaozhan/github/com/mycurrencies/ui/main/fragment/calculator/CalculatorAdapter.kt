package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseVBViewHolder
import mustafaozhan.github.com.mycurrencies.databinding.ItemCurrencyBinding
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CalculatorAdapter(
    val calculatorItemAction: CalculatorItemAction
) : BaseVBRecyclerViewAdapter<Currency, ItemCurrencyBinding>(
    CalculatorDiffer()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesViewBindingViewHolder(ItemCurrencyBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    fun submitList(list: MutableList<Currency>?, currentBase: Currencies) = submitList(
        list?.filter {
            it.name != currentBase.toString() &&
                it.isActive == 1 &&
                it.rate.toString() != "NaN" &&
                it.rate.toString() != "0.0"
        } ?: mutableListOf()
    )

    inner class RatesViewBindingViewHolder(itemBinding: ItemCurrencyBinding) :
        BaseVBViewHolder<Currency, ItemCurrencyBinding>(itemBinding) {

        override fun bindItem(item: Currency) {
            with(itemBinding) {
                txtType.text = item.name
                txtSymbol.text = item.symbol
                txtAmount.text = item.rate.getFormatted().replaceNonStandardDigits()
                imgItem.setBackgroundByName(item.name)
            }

            itemView.setOnClickListener {
                calculatorItemAction.switchBase(itemBinding.txtAmount.text.toString(), item)
            }

            itemView.setOnLongClickListener {
                calculatorItemAction.showCurrencyComparison(item)
                false
            }
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.base.adapter.BaseViewHolder
import mustafaozhan.github.com.mycurrencies.databinding.ItemCurrencyBinding
import mustafaozhan.github.com.mycurrencies.function.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.function.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.function.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CalculatorAdapter : BaseRecyclerViewAdapter<Currency, ItemCurrencyBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Currency, ItemCurrencyBinding> {
        binding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return RatesViewHolder(binding)
    }

    fun refreshList(list: MutableList<Currency>, currentBase: Currencies) =
        refreshList(list.filter {
            it.name != currentBase.toString() &&
                it.isActive == 1 &&
                it.rate.toString() != "NaN" &&
                it.rate.toString() != "0.0"
        }.toMutableList())

    inner class RatesViewHolder(binding: ItemCurrencyBinding) : BaseViewHolder<Currency, ItemCurrencyBinding>(binding) {
        override fun bindItem(item: Currency) {
            with(binding) {
                txtType.text = item.name
                txtSymbol.text = item.symbol
                txtAmount.text = item.rate.getFormatted().replaceNonStandardDigits()
                imgItem.setBackgroundByName(item.name)
            }
        }
    }
}

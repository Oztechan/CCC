package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.viewholder.BaseDBViewHolder
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view.CalculatorEvent

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
class CalculatorAdapter(
    private val calculatorViewEvent: CalculatorEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemCalculatorBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorDBViewHolder(ItemCalculatorBinding.inflate(
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

    inner class CalculatorDBViewHolder(itemBinding: ItemCalculatorBinding) :
        BaseDBViewHolder<Currency, ItemCalculatorBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.viewEvent = calculatorViewEvent
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}

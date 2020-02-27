package mustafaozhan.github.com.mycurrencies.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import kotlin.properties.Delegates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T, TViewBinding : ViewBinding>(
    private val compareFun: (T, T) -> Boolean = { o, n -> o == n }
) : RecyclerView.Adapter<BaseViewHolder<T, TViewBinding>>(), AutoUpdatableAdapter {

    lateinit var binding: TViewBinding

    private var items: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    var onItemClickListener: ((T, TViewBinding, Int) -> Unit) = { item, binding, position -> }
    var onItemLongClickListener: (T, TViewBinding) -> Boolean = { item, binding -> true }

    override fun onBindViewHolder(holder: BaseViewHolder<T, TViewBinding>, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
            itemView.setOnClickListener { onItemClickListener(item, binding, position) }
            itemView.setOnLongClickListener { onItemLongClickListener(item, binding) }
        }
    }

    fun refreshList(list: MutableList<T>, currentBase: Currencies? = null) {
        items = currentBase?.let {
            list.filter { listItem ->
                (listItem as? Currency)?.let {
                    it.name != currentBase.toString() &&
                        it.isActive == 1 &&
                        it.rate.toString() != "NaN" &&
                        it.rate.toString() != "0.0"
                } ?: false
            }.toMutableList()
        } ?: run { list }
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, TViewBinding>

    override fun getItemCount() = items.size

    private fun isEmpty(): Boolean = items.isEmpty()

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T, TViewBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}

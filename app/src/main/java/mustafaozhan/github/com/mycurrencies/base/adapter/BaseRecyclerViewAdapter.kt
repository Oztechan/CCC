package mustafaozhan.github.com.mycurrencies.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import kotlin.properties.Delegates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T>(
    private val compareFun: (T, T) -> Boolean = { o, n -> o == n }
) : RecyclerView.Adapter<BaseViewHolder<T>>(), AutoUpdatableAdapter {

    private var items: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    var onItemClickListener: ((T, View, Int) -> Unit) = { item, viewParent, position -> }
    var onItemLongClickListener: (T, View) -> Boolean = { item, viewParent -> true }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClickListener(item, holder.itemView, position) }
        holder.itemView.setOnLongClickListener { onItemLongClickListener(item, holder.itemView) }
    }

    protected fun getViewHolderView(parent: ViewGroup, @LayoutRes itemLayoutId: Int): View =
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)

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

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun getItemCount() = items.size

    private fun isEmpty(): Boolean = items.isEmpty()

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}
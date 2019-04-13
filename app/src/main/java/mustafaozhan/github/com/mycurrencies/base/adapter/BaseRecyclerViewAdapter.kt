package mustafaozhan.github.com.mycurrencies.base.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import kotlin.properties.Delegates


/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T>(private val compareFun: (T, T) -> Boolean = { o, n -> o == n })
    : RecyclerView.Adapter<BaseViewHolder<T>>(), AutoUpdatableAdapter {

    private var items: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    var onItemClickListener: ((T, View, Int) -> Unit) = { item, viewParent, position -> }
    var onItemLongClickListener: (T, View) -> Boolean = { item, viewParent -> true }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.fall_down
        ))

        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClickListener(item, holder.itemView, position) }
        holder.itemView.setOnLongClickListener { onItemLongClickListener(item, holder.itemView) }
    }

    protected fun getViewHolderView(parent: ViewGroup, @LayoutRes itemLayoutId: Int): View =
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)

    fun refreshList(list: MutableList<T>, currentBase: Currencies? = null) {
        items = if (currentBase != null && list.checkItemsAre<Currency>()) {
            list.filter { listItem ->
                listItem as Currency
                listItem.name != currentBase.toString() &&
                    listItem.isActive == 1 &&
                    listItem.rate.toString() != "NaN" &&
                    listItem.rate.toString() != "0.0"
            }.toMutableList()
        } else list
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun getItemCount() = items.size

    fun isEmpty(): Boolean = items.isEmpty()

    private inline fun <reified T : Any> MutableList<*>.checkItemsAre() =
        all { it is T }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}
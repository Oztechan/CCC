package mustafaozhan.github.com.mycurrencies.base.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    private var items: MutableList<T> = mutableListOf()

    var onItemClickListener: ((
        T,
        view: View,
        viewParent: View,
        position: Int
    ) -> Unit
    ) = { t: T,
          view: View,
          viewParent: View,
          position: Int ->
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {

        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClickListener(item, it, it, position) }
        getAllChildren(holder.itemView).forEach { view ->
            view.setOnClickListener {
                onItemClickListener(item, it, holder.itemView, position)
            }
        }
    }

    private fun getAllChildren(v: View): ArrayList<View> {

        if (v !is ViewGroup) {
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }

        val result = ArrayList<View>()

        for (i in 0 until v.childCount) {
            val child = v.getChildAt(i)
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            viewArrayList.addAll(getAllChildren(child))
            result.addAll(viewArrayList.filter { view -> view.isClickable }.toList())
        }

        return result
    }

    override fun getItemCount() = items.size

    fun isEmpty(): Boolean = items.isEmpty()

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
        } else {
            list
        }
        notifyDataSetChanged()
    }

    private inline fun <reified T : Any> MutableList<*>.checkItemsAre() =
        all { it is T }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>
}
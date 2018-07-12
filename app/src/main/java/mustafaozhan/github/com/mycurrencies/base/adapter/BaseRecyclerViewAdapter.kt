package mustafaozhan.github.com.mycurrencies.base.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T>(private val compareFun: (T, T) -> Boolean = { o, n -> o == n }) : RecyclerView.Adapter<BaseViewHolder<T>>(), AutoUpdatableAdapter {

    private var items: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    fun isEmpty(): Boolean = items.isEmpty()


    private var onItemSelectedListener: ((T, view: View, viewParent: View) -> Unit) = { t: T, view: View, viewParent: View -> }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemSelectedListener(item, it, it) }
        getAllChildren(holder.itemView).forEach { view -> view.setOnClickListener { onItemSelectedListener(item, it, holder.itemView) } }
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

    protected fun getViewHolderView(parent: ViewGroup, @LayoutRes itemLayoutId: Int): View =
            LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)

    fun refreshList(newList: MutableList<T>) {
        items = newList
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>
}
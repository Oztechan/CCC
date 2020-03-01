package mustafaozhan.github.com.mycurrencies.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.Delegates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T, TViewBinding : ViewBinding> :
    RecyclerView.Adapter<BaseViewHolder<T, TViewBinding>>(), AutoUpdatableAdapter {

    abstract var binding: TViewBinding
    abstract fun bind(parent: ViewGroup)

    private var list: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    internal var onItemClickListener: ((T, TViewBinding, Int) -> Unit)? = null
    internal var onItemLongClickListener: ((T, TViewBinding) -> Boolean)? = null

    protected fun getViewHolderBinding(parent: ViewGroup): TViewBinding {
        bind(parent)
        return binding
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T, TViewBinding>, position: Int) {
        val item = list[position]
        holder.apply {
            bindItem(item)

            onItemClickListener?.let { listener ->
                itemView.setOnClickListener { listener(item, holderBinding, position) }
            }
            onItemLongClickListener?.let { listener ->
                itemView.setOnLongClickListener { listener(item, holderBinding) }
            }
        }
    }

    override fun getItemCount() = list.size

    internal fun refreshList(list: MutableList<T>) {
        this.list = list
    }
}

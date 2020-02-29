package mustafaozhan.github.com.mycurrencies.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.Delegates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseRecyclerViewAdapter<T, TViewBinding : ViewBinding>(
    private val compareFun: (T, T) -> Boolean = { o, n -> o == n }
) : RecyclerView.Adapter<BaseViewHolder<T, TViewBinding>>(), AutoUpdatableAdapter {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, TViewBinding>
    lateinit var binding: TViewBinding

    private var list: MutableList<T> by Delegates.observable(mutableListOf()) { _, old, new ->
        autoNotify(old, new) { o, n -> compareFun(o, n) }
    }

    var onItemClickListener: ((T, TViewBinding, Int) -> Unit)? = null
    var onItemLongClickListener: ((T, TViewBinding) -> Boolean)? = null

    override fun onBindViewHolder(holder: BaseViewHolder<T, TViewBinding>, position: Int) {
        val item = list[position]
        holder.apply {
            bind(item)

            onItemClickListener?.let { listener ->
                itemView.setOnClickListener { listener(item, binding, position) }
            }
            onItemLongClickListener?.let { listener ->
                itemView.setOnLongClickListener { listener(item, binding) }
            }
        }
    }

    internal fun refreshList(list: MutableList<T>) {
        this.list = list
    }

    override fun getItemCount() = list.size
}

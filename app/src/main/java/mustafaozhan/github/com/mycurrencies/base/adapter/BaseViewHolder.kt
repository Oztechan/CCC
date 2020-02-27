package mustafaozhan.github.com.mycurrencies.base.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseViewHolder<out T, TViewBinding : ViewBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var binding: TViewBinding

    abstract fun bind(item: @UnsafeVariance T)

    val context: Context = itemView.context

    fun getString(resId: Int, vararg formatArgs: Any? = emptyArray()): String {
        return context.getString(resId, formatArgs)
    }
}

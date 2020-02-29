package mustafaozhan.github.com.mycurrencies.base.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
abstract class BaseViewHolder<out T, TViewBinding : ViewBinding>(binding: TViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bindItem(item: @UnsafeVariance T)
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDBRecyclerViewAdapter<T, TDataBinding : ViewDataBinding>(
    itemDiffer: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseDBRecyclerViewAdapter.BaseDBViewHolder<T, TDataBinding>>(itemDiffer) {
    override fun onBindViewHolder(
        holder: BaseDBViewHolder<T, TDataBinding>,
        position: Int
    ) = with(holder) {
        onItemBind(getItem(position))
        itemBinding.executePendingBindings()
    }

    abstract class BaseDBViewHolder<out T, TDataBinding : ViewDataBinding>(
        val itemBinding: TDataBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        abstract fun onItemBind(item: @UnsafeVariance T)
    }
}

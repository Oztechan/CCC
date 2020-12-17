/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingComponent
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange
import androidx.lifecycle.LifecycleCoroutineScope
import com.github.mustafaozhan.ccc.android.model.DataState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R


class AppBindingComponent(private val scope: LifecycleCoroutineScope) : DataBindingComponent {
    @BindingAdapter("visibleIf")
    fun View.visibleIf(condition: StateFlow<Boolean>) = scope.launchWhenStarted {
        condition.collect {
            visibility = if (it) {
                bringToFront()
                View.VISIBLE
            } else View.GONE
        }
    }

    @BindingAdapter("goneIf")
    fun View.goneIf(condition: StateFlow<Boolean>) = scope.launchWhenStarted {
        condition.collect {
            visibility = if (it) View.GONE else {
                bringToFront()
                View.VISIBLE
            }
        }
    }

    override fun getAppBindingComponent(): AppBindingComponent {
        return AppBindingComponent(scope)
    }
}

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) {
    visibility = if (visible) {
        bringToFront()
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("dataState")
fun TextView.dataState(state: DataState) = when (state) {
    is DataState.Online -> {
        text = context.getString(R.string.text_online_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0)
    }
    is DataState.Cached -> {
        text = context.getString(R.string.text_cached_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cached, 0, 0, 0)
    }
    is DataState.Offline -> {
        text = if (state.lastUpdate.isNullOrEmpty()) {
            context.getString(R.string.text_offline)
        } else {
            context.getString(R.string.text_offline_last_updated, state.lastUpdate)
        }
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offine, 0, 0, 0)
    }
    DataState.Error -> {
        text = context.getString(R.string.text_no_data)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
    }
}

@BindingAdapter("backgroundByName")
fun ImageView.backgroundByName(base: String) = setBackgroundByName(base)

@BindingAdapter("onQueryTextChange")
fun SearchView.setOnQueryTextListener(change: OnQueryTextChange) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false
        override fun onQueryTextChange(newText: String?) = change.onQueryTextChange(newText)
    })
}

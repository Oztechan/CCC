/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.main.calculator.Cached
import com.github.mustafaozhan.ui.main.calculator.DataState
import com.github.mustafaozhan.ui.main.calculator.Error
import com.github.mustafaozhan.ui.main.calculator.Offline
import com.github.mustafaozhan.ui.main.calculator.Online

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
    is Online -> {
        text = context.getString(R.string.text_online_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0)
    }
    is Cached -> {
        text = context.getString(R.string.text_cached_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cached, 0, 0, 0)
    }
    is Offline -> {
        text = context.getString(R.string.text_offline_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offine, 0, 0, 0)
    }
    Error -> {
        text = context.getString(R.string.text_offline)
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

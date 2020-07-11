/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.util

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) {
    visibility = if (visible) {
        bringToFront()
        View.VISIBLE
    } else {
        View.GONE
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

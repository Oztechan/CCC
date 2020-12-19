/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused", "TooManyFunctions")

package com.github.mustafaozhan.ccc.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.scopemob.castTo
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.io.FileNotFoundException
import java.util.Locale
import mustafaozhan.github.com.mycurrencies.R

fun ImageView.setBackgroundByName(name: String) =
    setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int = try {
    resources.getIdentifier(
        name.toLowerCase(Locale.getDefault()).replace("try", "tryy"),
        "drawable",
        packageName
    )
} catch (e: FileNotFoundException) {
    kermit.w(e) { e.message.toString() }
    R.drawable.transparent
}

fun View.hideKeyboard() = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
    ?.castTo<InputMethodManager>()
    ?.hideSoftInputFromWindow(windowToken, 0)
    ?.toUnit()

fun FrameLayout.setAdaptiveBannerAd(adId: String, isExpired: Boolean) = if (isExpired) {
    MobileAds.initialize(context)
    with(context.resources.displayMetrics) {

        var adWidthPixels = width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = widthPixels.toFloat()
        }
        addView(
            AdView(context).apply {
                adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    (adWidthPixels / density).toInt()
                )
                adUnitId = adId
                loadAd(AdRequest.Builder().build())
            }
        )
    }
} else {
    isEnabled = false
    visibility = View.GONE
}

fun <T> Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

// todo here needs to be changed
@SuppressLint("RestrictedApi")
fun <T> Fragment.setNavigationResult(destinationId: Int, result: T, key: String) =
    findNavController()
        .backStack
        .firstOrNull { it.destination.id == destinationId }
        ?.savedStateHandle?.set(key, result)

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun View.visibleIf(visible: Boolean) {
    visibility = if (visible) {
        bringToFront()
        View.VISIBLE
    } else {
        View.GONE
    }
}

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

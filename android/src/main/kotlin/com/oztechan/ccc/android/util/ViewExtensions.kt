/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.oztechan.ccc.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.logmob.e
import com.github.submob.scopemob.castTo
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.client.model.RateState
import mustafaozhan.github.com.mycurrencies.R
import java.io.FileNotFoundException

private const val ANIMATION_DURATION = 500L

fun ImageView.setBackgroundByName(
    name: String
) = setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int = try {
    resources.getIdentifier(
        name.lowercase().replace("try", "tryy"),
        "drawable",
        packageName
    )
} catch (e: FileNotFoundException) {
    Logger.e(e)
    R.drawable.transparent
}

fun View.hideKeyboard() = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
    ?.castTo<InputMethodManager>()
    ?.hideSoftInputFromWindow(windowToken, 0)

fun FrameLayout.setBannerAd(
    adManager: AdManager,
    adId: String,
    shouldShowAd: Boolean
) = if (shouldShowAd) {
    removeAllViews()

    addView(
        adManager.getBannerAd(context, width, adId) { height ->
            if (height != null) animateHeight(0, height)
            visible()
        }
    )
} else {
    gone()
}

fun View.animateHeight(startHeight: Int, endHeight: Int) {
    layoutParams.height = 0

    val delta = endHeight - startHeight
    var newHeight: Int
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            newHeight = (startHeight + delta * interpolatedTime).toInt()
            layoutParams.height = newHeight
            requestLayout()
        }
    }
    animation.duration = ANIMATION_DURATION
    startAnimation(animation)
}

fun <T> Fragment.getNavigationResult(
    key: String
) = findNavController()
    .currentBackStackEntry
    ?.savedStateHandle
    ?.getLiveData<T>(key)

// todo here needs to be changed
@SuppressLint("RestrictedApi")
fun <T> Fragment.setNavigationResult(
    destinationId: Int,
    result: T,
    key: String
) = findNavController()
    .backStack
    .firstOrNull { it.destination.id == destinationId }
    ?.savedStateHandle?.set(key, result)

fun View?.visibleIf(visible: Boolean) = if (visible) visible() else gone()

fun View.showLoading(visible: Boolean) = if (visible) {
    visible()
    bringToFront()
} else gone()

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun TextView.dataState(state: RateState) = when (state) {
    is RateState.Online -> {
        text = context.getString(R.string.text_online_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0)
        visible()
    }
    is RateState.Cached -> {
        text = context.getString(R.string.text_cached_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cached, 0, 0, 0)
        visible()
    }
    is RateState.Offline -> {
        text = if (state.lastUpdate.isNullOrEmpty()) {
            context.getString(R.string.text_offline)
        } else {
            context.getString(R.string.text_offline_last_updated, state.lastUpdate)
        }
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offine, 0, 0, 0)
        visible()
    }
    RateState.Error -> {
        text = context.getString(R.string.text_no_data)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
        visible()
    }
    RateState.None -> gone()
}

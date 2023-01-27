/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.oztechan.ccc.android.feature.mobile.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.submob.scopemob.castTo
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.core.ad.BannerAdView
import com.oztechan.ccc.android.feature.mobile.R
import com.oztechan.ccc.client.core.res.getImageIdByName
import com.oztechan.ccc.client.model.ConversionState

private const val ANIMATION_DURATION = 500L

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
            isVisible = true
        }
    )
} else {
    isGone = true
}

fun FrameLayout.destroyBanner() {
    children.firstOrNull()?.castTo<BannerAdView>()?.onDestroy?.invoke()
    removeAllViews()
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
    key: String,
    destinationId: Int
) = findNavController()
    .backQueue
    .lastOrNull { it.destination.id == destinationId }
    ?.savedStateHandle
    ?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(
    destinationId: Int,
    result: T,
    key: String
) = findNavController()
    .backQueue
    .lastOrNull { it.destination.id == destinationId }
    ?.savedStateHandle
    ?.set(key, result)

fun View?.visibleIf(visible: Boolean, bringFront: Boolean = false) = this?.apply {
    if (visible) {
        isVisible = true
        if (bringFront) bringToFront()
    } else {
        isGone = true
    }
}

fun TextView.dataState(state: ConversionState) = when (state) {
    is ConversionState.Online -> {
        text = context.getString(R.string.text_online_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0)
        isVisible = true
    }

    is ConversionState.Cached -> {
        text = context.getString(R.string.text_cached_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cached, 0, 0, 0)
        isVisible = true
    }

    is ConversionState.Offline -> {
        text = if (state.lastUpdate.isNullOrEmpty()) {
            context.getString(R.string.text_offline)
        } else {
            context.getString(R.string.text_offline_last_updated, state.lastUpdate)
        }
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offine, 0, 0, 0)
        isVisible = true
    }

    ConversionState.Error -> {
        text = context.getString(R.string.text_no_data)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
        isVisible = true
    }

    ConversionState.None -> isGone = true
}

private const val CLIPBOARD_LABEL = "clipboard_label"

fun View.copyToClipBoard(text: String) {
    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText(CLIPBOARD_LABEL, text)

    clipboard?.setPrimaryClip(clip)?.let {
        showSnack(context.getString(R.string.copied_to_clipboard))
    }
}

fun ImageView.setBackgroundByName(name: String) = setImageResource(name.getImageIdByName())

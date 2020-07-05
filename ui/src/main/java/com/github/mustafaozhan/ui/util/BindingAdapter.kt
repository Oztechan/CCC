/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.SearchViewBindingAdapter.OnQueryTextChange
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@BindingAdapter("adId", "isExpired")
fun FrameLayout.adAdapter(adId: String, isExpired: Boolean) = if (isExpired) {
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

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) {
    visibility = if (visible) {
        bringToFront()
        if (visibility != View.VISIBLE) startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        View.VISIBLE
    } else {
        if (visibility != View.GONE) startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
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

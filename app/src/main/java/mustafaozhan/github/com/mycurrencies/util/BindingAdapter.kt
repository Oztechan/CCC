/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.util.extension.setBackgroundByName

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
fun View.visibility(visible: Boolean) = if (visible) {
    bringToFront()
    visibility = View.VISIBLE
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
} else {
    visibility = View.GONE
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}

@BindingAdapter("backgroundByName")
fun ImageView.backgroundByName(base: String) = setBackgroundByName(base)

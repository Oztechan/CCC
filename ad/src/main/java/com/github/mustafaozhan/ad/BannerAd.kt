package com.github.mustafaozhan.ad

import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

fun ViewGroup.loadBannerAd(
    adId: String
) = with(context.applicationContext) {
    var adWidthPixels = width.toFloat()

    if (adWidthPixels == 0f) {
        adWidthPixels = resources.displayMetrics.widthPixels.toFloat()
    }

    removeAllViews()
    addView(
        AdView(this).apply {
            adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                context,
                (adWidthPixels / resources.displayMetrics.density).toInt()
            )
            adUnitId = adId
            loadAd(AdRequest.Builder().build())
        }
    )
}

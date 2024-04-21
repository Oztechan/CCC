package com.oztechan.ccc.android.core.ad

import android.app.Activity
import android.content.Context

interface AdManager {

    fun initAds(activity: Activity)

    fun getBannerAd(
        context: Context,
        width: Int,
        adId: String,
        onAdLoaded: (Int?) -> Unit
    ): BannerAdView

    fun showInterstitialAd(
        activity: Activity,
        adId: String
    )

    fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onReward: () -> Unit
    )
}

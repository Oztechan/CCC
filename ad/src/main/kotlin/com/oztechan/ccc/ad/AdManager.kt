package com.oztechan.ccc.ad

import android.app.Activity
import android.content.Context

interface AdManager {

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
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    )
}

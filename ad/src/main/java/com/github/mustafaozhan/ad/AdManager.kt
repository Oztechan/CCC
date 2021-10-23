package com.github.mustafaozhan.ad

import android.app.Activity
import android.view.ViewGroup

interface AdManager {

    fun loadBannerAd(
        viewGroup: ViewGroup,
        adId: String
    )

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

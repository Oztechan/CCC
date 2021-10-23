package com.github.mustafaozhan.ad

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout

class AdManagerImpl(private val context: Context) : AdManager {

    override fun getBannerAd(width: Int, adId: String): ViewGroup {
        return FrameLayout(context)
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) = Unit

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    ) = Unit
}

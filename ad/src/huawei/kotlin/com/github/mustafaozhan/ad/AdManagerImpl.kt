package com.github.mustafaozhan.ad

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import co.touchlab.kermit.Logger

class AdManagerImpl : AdManager {

    override fun getBannerAd(
        context: Context,
        width: Int,
        adId: String,
        onAdLoaded: (Int?) -> Unit
    ): ViewGroup {
        Logger.i { "AdManagerImpl getBannerAd" }
        return FrameLayout(context)
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) {
        Logger.i { "AdManagerImpl showInterstitialAd" }
    }

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    ) {
        Logger.i { "AdManagerImpl showRewardedAd" }
    }
}

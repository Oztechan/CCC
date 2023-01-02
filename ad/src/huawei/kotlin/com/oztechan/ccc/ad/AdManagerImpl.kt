package com.oztechan.ccc.ad

import android.app.Activity
import android.content.Context
import android.view.View
import co.touchlab.kermit.Logger

internal class AdManagerImpl : AdManager {

    override fun getBannerAd(
        context: Context,
        width: Int,
        adId: String,
        onAdLoaded: (Int?) -> Unit
    ): BannerAdView {
        Logger.i { "AdManagerImpl getBannerAd" }
        return BannerAdView(context, View(context)) { }
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

package com.oztechan.ccc.android.core.ad

import android.app.Activity
import android.content.Context
import android.view.View
import co.touchlab.kermit.Logger
import com.huawei.hms.ads.HwAds

internal class AdManagerImpl : AdManager {

    init {
        HwAds.setVideoVolume(0f)
        HwAds.setVideoMuted(true)
    }

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

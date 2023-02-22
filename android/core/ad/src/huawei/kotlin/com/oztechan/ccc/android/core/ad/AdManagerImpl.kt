package com.oztechan.ccc.android.core.ad

import android.app.Activity
import android.content.Context
import co.touchlab.kermit.Logger
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView

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

        val adView = BannerView(context).apply {
            this.adId = "testw6vs28auh3"
            bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdImpression()
                    onAdLoaded(bannerAdSize.getHeightPx(context))
                }
            }
            loadAd(AdParam.Builder().build())
        }

        return BannerAdView(context, banner = adView) { adView.destroy() }
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

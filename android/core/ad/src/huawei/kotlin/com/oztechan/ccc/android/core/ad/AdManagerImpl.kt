package com.oztechan.ccc.android.core.ad

import android.app.Activity
import android.content.Context
import co.touchlab.kermit.Logger
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.ads.banner.BannerView

internal class AdManagerImpl : AdManager {

    private val adParam: AdParam by lazy {
        AdParam.Builder().build()
    }

    init {
        Logger.i { "AdManagerImpl init" }
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
            this.adId = adId
            bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdImpression()
                    onAdLoaded(bannerAdSize.getHeightPx(context))
                }
            }
            loadAd(adParam)
        }

        return BannerAdView(context, banner = adView) { adView.destroy() }
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) {
        Logger.i { "AdManagerImpl showInterstitialAd" }
        InterstitialAd(activity).apply {
            this.adId = adId
            adListener = object : AdListener() {
                override fun onAdFailed(adError: Int) {
                    super.onAdFailed(adError)
                    Logger.w { "AdManagerImpl showInterstitialAd onAdFailed $adError" }
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Logger.i { "AdManagerImpl showInterstitialAd onAdLoaded" }
                    show(activity)
                }
            }
            loadAd(adParam)
        }
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

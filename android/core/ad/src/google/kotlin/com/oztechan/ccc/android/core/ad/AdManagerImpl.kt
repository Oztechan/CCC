package com.oztechan.ccc.android.core.ad

import android.app.Activity
import android.content.Context
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

internal class AdManagerImpl : AdManager {

    private val adRequest: AdRequest by lazy {
        AdRequest.Builder().build()
    }

    init {
        Logger.v { "AdManagerImpl init" }
        MobileAds.setAppVolume(0.0f)
        MobileAds.setAppMuted(true)
    }

    override fun getBannerAd(
        context: Context,
        width: Int,
        adId: String,
        onAdLoaded: (Int?) -> Unit
    ): BannerAdView {
        Logger.v { "AdManagerImpl getBannerAd" }

        val adView = AdView(context).apply {
            val adWidthPixels = if (width == 0) {
                context.resources.displayMetrics.widthPixels.toFloat()
            } else {
                width.toFloat()
            }

            setAdSize(
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    (adWidthPixels / resources.displayMetrics.density).toInt()
                )
            )
            adUnitId = adId
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    onAdLoaded(adSize?.height?.times(resources.displayMetrics.density)?.toInt())
                }
            }
            loadAd(adRequest)
        }
        return BannerAdView(context, banner = adView) { adView.destroy() }
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) {
        Logger.v { "AdManagerImpl showInterstitialAd" }

        InterstitialAd.load(
            activity,
            adId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Logger.e { "AdManagerImpl showInterstitialAd onAdFailedToLoad ${adError.message}" }
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    Logger.v { "AdManagerImpl showInterstitialAd onAdLoaded" }
                    interstitialAd.show(activity)
                }
            }
        )
    }

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onReward: () -> Unit
    ) {
        Logger.v { "AdManagerImpl showRewardedAd" }

        RewardedAd.load(
            activity,
            adId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Logger.e { "AdManagerImpl showRewardedAd onAdFailedToLoad ${adError.message}" }
                    onAdFailedToLoad()
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Logger.v { "AdManagerImpl showRewardedAd onAdLoaded" }

                    rewardedAd.show(activity) {
                        Logger.v { "AdManagerImpl showRewardedAd onUserEarnedReward" }
                        onReward()
                    }
                }
            }
        )
    }
}

package com.github.mustafaozhan.ad

import android.app.Activity
import android.content.Context
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManagerImpl : AdManager {

    init {
        Logger.i { "AdManagerImpl init" }
        MobileAds.setAppVolume(0.0f)
        MobileAds.setAppMuted(true)
    }

    override fun getBannerAd(
        context: Context,
        width: Int,
        adId: String
    ) = AdView(context).apply {
        MobileAds.initialize(context)
        Logger.i { "AdManagerImpl getBannerAd" }


        val adWidthPixels = if (width == 0) {
            context.resources.displayMetrics.widthPixels.toFloat()
        } else {
            width.toFloat()
        }


        adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context,
            (adWidthPixels / resources.displayMetrics.density).toInt()
        )
        adUnitId = adId
        loadAd(getAdRequest())
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) = InterstitialAd.load(
        activity,
        adId,
        getAdRequest(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Logger.w { "AdManagerImpl onAdFailedToLoad ${adError.message}" }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Logger.i { "AdManagerImpl onAdLoaded" }
                interstitialAd.show(activity)
            }
        }
    ).also {
        Logger.i { "AdManagerImpl showInterstitialAd" }
    }

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    ) = RewardedAd.load(
        activity,
        adId,
        getAdRequest(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Logger.w { "AdManagerImpl onAdFailedToLoad ${adError.message}" }
                onAdFailedToLoad()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Logger.i { "AdManagerImpl onAdLoaded" }
                onAdLoaded()

                rewardedAd.show(activity) {
                    Logger.i { "AdManagerImpl onUserEarnedReward" }
                    onReward()
                }
            }
        }
    ).also {
        Logger.i { "AdManagerImpl showRewardedAd" }
    }

    private fun getAdRequest() = AdRequest.Builder().build()
}

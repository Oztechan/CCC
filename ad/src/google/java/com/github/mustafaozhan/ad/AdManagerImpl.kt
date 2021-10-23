package com.github.mustafaozhan.ad

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.github.mustafaozhan.logmob.kermit
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManagerImpl(private val context: Context) : AdManager {

    init {
        MobileAds.initialize(context)
    }

    override fun getBannerAd(
        width: Int,
        adId: String
    ): ViewGroup {
        var adWidthPixels = width.toFloat()

        if (adWidthPixels == 0f) {
            adWidthPixels = context.resources.displayMetrics.widthPixels.toFloat()
        }

        return AdView(context).apply {
            adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                context,
                (adWidthPixels / resources.displayMetrics.density).toInt()
            )
            adUnitId = adId
            loadAd(getAdRequest())
        }
    }

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) = InterstitialAd.load(
        context,
        adId,
        getAdRequest(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                kermit.d { "InterstitialAd onAdFailedToLoad ${adError.message}" }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                kermit.d { "InterstitialAd onAdLoaded" }
                interstitialAd.show(activity)
            }
        })

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    ) = RewardedAd.load(
        context,
        adId,
        getAdRequest(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                kermit.d { "RewardedAd onRewardedAdFailedToLoad" }
                onAdFailedToLoad()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                kermit.d { "RewardedAd onRewardedAdLoaded" }
                onAdLoaded()

                rewardedAd.show(activity) {
                    kermit.d { "RewardedAd onUserEarnedReward" }
                    onReward()
                }
            }
        }
    )

    private fun getAdRequest() = AdRequest.Builder().build()
}

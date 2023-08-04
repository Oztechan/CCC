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
import com.huawei.hms.ads.reward.Reward
import com.huawei.hms.ads.reward.RewardAd
import com.huawei.hms.ads.reward.RewardAdLoadListener
import com.huawei.hms.ads.reward.RewardAdStatusListener

internal class AdManagerImpl : AdManager {

    private val adParam: AdParam by lazy {
        AdParam.Builder().build()
    }

    init {
        Logger.v { "AdManagerImpl init" }
        HwAds.setVideoVolume(0f)
        HwAds.setVideoMuted(true)
    }

    override fun getBannerAd(
        context: Context,
        width: Int,
        adId: String,
        onAdLoaded: (Int?) -> Unit
    ): BannerAdView {
        Logger.v { "AdManagerImpl getBannerAd" }

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
        Logger.v { "AdManagerImpl showInterstitialAd" }
        InterstitialAd(activity).apply {
            this.adId = adId
            adListener = object : AdListener() {
                override fun onAdFailed(adError: Int) {
                    super.onAdFailed(adError)
                    Logger.w { "AdManagerImpl showInterstitialAd onAdFailed $adError" }
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Logger.v { "AdManagerImpl showInterstitialAd onAdLoaded" }
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
        Logger.v { "AdManagerImpl showRewardedAd" }

        RewardAd(activity, adId).apply {
            loadAd(
                adParam,
                object : RewardAdLoadListener() {
                    override fun onRewardAdFailedToLoad(adError: Int) {
                        super.onRewardAdFailedToLoad(adError)
                        Logger.w { "AdManagerImpl showRewardedAd onRewardAdFailedToLoad $adError" }
                        onAdFailedToLoad()
                    }

                    override fun onRewardedLoaded() {
                        super.onRewardedLoaded()
                        Logger.v { "AdManagerImpl showRewardedAd onRewardedLoaded" }
                        onAdLoaded()

                        show(
                            activity,
                            object : RewardAdStatusListener() {
                                override fun onRewarded(reward: Reward?) {
                                    super.onRewarded(reward)
                                    Logger.v { "AdManagerImpl showRewardedAd onRewardedLoaded onRewarded" }
                                    onReward()
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}

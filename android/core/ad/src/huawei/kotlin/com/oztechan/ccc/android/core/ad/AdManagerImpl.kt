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
    }

    override fun initAds(activity: Activity) {
        Logger.v { "Ads initAds" }
        HwAds.init(activity)
        HwAds.setVideoVolume(0f)
        HwAds.setVideoMuted(true)
    }

    override fun isPrivacyOptionsRequired() = false

    override fun showConsentForm(activity: Activity) = Unit

    override fun getBannerAd(
        context: Context,
        adId: String,
        maxHeight: Int
    ): BannerAdView {
        Logger.v { "AdManagerImpl getBannerAd" }

        val adView = BannerView(context).apply {
            this.adId = adId
            bannerAdSize = BannerAdSize.BANNER_SIZE_SMART
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
                    Exception("AdManagerImpl showInterstitialAd onAdFailed $adError").let {
                        Logger.e(it) { it.message.orEmpty() }
                    }
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
        onReward: () -> Unit
    ) {
        Logger.v { "AdManagerImpl showRewardedAd" }

        RewardAd(activity, adId).apply {
            loadAd(
                adParam,
                object : RewardAdLoadListener() {
                    override fun onRewardAdFailedToLoad(adError: Int) {
                        super.onRewardAdFailedToLoad(adError)
                        Exception("AdManagerImpl showRewardedAd onRewardAdFailedToLoad $adError").let {
                            Logger.e(it) { it.message.orEmpty() }
                        }
                        onAdFailedToLoad()
                    }

                    override fun onRewardedLoaded() {
                        super.onRewardedLoaded()
                        Logger.v { "AdManagerImpl showRewardedAd onRewardedLoaded" }

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

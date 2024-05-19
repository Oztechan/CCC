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
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

internal class AdManagerImpl(context: Context) : AdManager {
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    private val adRequest: AdRequest by lazy {
        AdRequest.Builder().build()
    }

    init {
        Logger.v { "AdManagerImpl init" }
    }

    override fun initAds(activity: Activity) {
        Logger.v { "AdManagerImpl initAds" }
        consentInformation.requestConsentInfoUpdate(
            activity,
            ConsentRequestParameters.Builder().build(),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    if (it != null) {
                        Exception("Consent gathering failed: ${it.errorCode}: ${it.message}").let { exception ->
                            Logger.e(exception) { exception.message.orEmpty() }
                        }
                    }

                    // Consent has been gathered.
                    if (consentInformation.canRequestAds()) {
                        activity.initializeMobileAdsSdk()
                    }
                }
            },
            {
                Exception("Consent gathering failed: ${it.errorCode}: ${it.message}").let { exception ->
                    Logger.e(exception) { exception.message.orEmpty() }
                }
            }
        )

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            activity.initializeMobileAdsSdk()
        }
    }

    override fun isPrivacyOptionsRequired() =
        consentInformation.privacyOptionsRequirementStatus ==
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    override fun showConsentForm(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) {
            if (it != null) {
                Exception("Showing consent form failed: ${it.errorCode}: ${it.message}").let { exception ->
                    Logger.e(exception) { exception.message.orEmpty() }
                }
            }
        }
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
                    Exception("AdManagerImpl showInterstitialAd onAdFailedToLoad ${adError.message}").let {
                        Logger.e(it) { it.message.orEmpty() }
                    }
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
                    Exception("AdManagerImpl showRewardedAd onAdFailedToLoad ${adError.message}").let {
                        Logger.e(it) { it.message.orEmpty() }
                    }
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

    private fun Activity.initializeMobileAdsSdk() {
        Logger.v { "AdManagerImpl initializeMobileAdsSdk" }

        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            Logger.v { "AdManagerImpl initializeMobileAdsSdk is not called, already called" }
            return
        }

        MobileAds.initialize(this)
        MobileAds.setAppVolume(0.0f)
        MobileAds.setAppMuted(true)
    }
}

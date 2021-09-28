package com.github.mustafaozhan.ad

import android.app.Activity
import com.github.mustafaozhan.logmob.kermit
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

fun Activity.showInterstitialAd(
    adId: String
) = InterstitialAd.load(
    this,
    adId,
    AdRequest.Builder().build(),
    object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            kermit.d { "InterstitialAd onAdFailedToLoad ${adError.message}" }
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            kermit.d { "InterstitialAd onAdLoaded" }
            interstitialAd.show(this@showInterstitialAd)
        }
    })

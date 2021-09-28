package com.github.mustafaozhan.ad

import android.app.Activity
import com.github.mustafaozhan.logmob.kermit
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

fun Activity.showRewardedAd(
    adId: String,
    onAdFailedToLoad: () -> Unit,
    onAdLoaded: () -> Unit,
    onReward: () -> Unit
) = RewardedAd.load(
    this,
    adId,
    AdRequest.Builder().build(),
    object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            kermit.d { "RewardedAd onRewardedAdFailedToLoad" }
            onAdFailedToLoad()
        }

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            kermit.d { "RewardedAd onRewardedAdLoaded" }
            onAdLoaded()

            rewardedAd.show(this@showRewardedAd) {
                kermit.d { "RewardedAd onUserEarnedReward" }
                onReward()
            }
        }
    }
)
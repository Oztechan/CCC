package com.github.mustafaozhan.ad

import android.content.Context
import android.view.ViewGroup

class AdManagerImpl(private val context: Context) : AdManager {

    override fun loadBannerAd(
        viewGroup: ViewGroup,
        adId: String
    ) = Unit

    override fun showInterstitialAd(
        activity: Activity,
        adId: String
    ) = Unit

    override fun showRewardedAd(
        activity: Activity,
        adId: String,
        onAdFailedToLoad: () -> Unit,
        onAdLoaded: () -> Unit,
        onReward: () -> Unit
    ) = Unit
}

package com.oztechan.ccc.client.repository.ad

interface AdRepository {
    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean

    fun shouldShowRemoveAds(): Boolean
}

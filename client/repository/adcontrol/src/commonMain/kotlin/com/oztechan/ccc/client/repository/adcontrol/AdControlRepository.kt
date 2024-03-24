package com.oztechan.ccc.client.repository.adcontrol

interface AdControlRepository {
    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean
}

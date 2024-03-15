package com.oztechan.ccc.client.repository.adcontrol

interface AdControlRepository {
    suspend fun shouldShowBannerAd(): Boolean

    suspend fun shouldShowInterstitialAd(): Boolean
}

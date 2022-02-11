package com.github.mustafaozhan.ccc.client.helper

interface SessionManager {
    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean
}

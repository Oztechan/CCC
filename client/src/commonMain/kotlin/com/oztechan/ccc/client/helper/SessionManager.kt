package com.oztechan.ccc.client.helper

interface SessionManager {
    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean

    fun checkAppUpdate(isAppUpdateShown: Boolean): Boolean?

    fun shouldShowAppReview(): Boolean
}

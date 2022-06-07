package com.oztechan.ccc.client.manager.session

interface SessionManager {
    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean

    fun checkAppUpdate(isAppUpdateShown: Boolean): Boolean?

    fun shouldShowAppReview(): Boolean
}

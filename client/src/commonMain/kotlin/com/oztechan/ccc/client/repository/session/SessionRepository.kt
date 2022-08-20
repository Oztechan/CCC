package com.oztechan.ccc.client.repository.session

import com.oztechan.ccc.client.model.Device

interface SessionRepository {
    val device: Device

    fun shouldShowBannerAd(): Boolean

    fun shouldShowInterstitialAd(): Boolean

    fun checkAppUpdate(isAppUpdateShown: Boolean): Boolean?

    fun shouldShowAppReview(): Boolean

    fun shouldShowRemoveAds(): Boolean
}

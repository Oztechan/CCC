package com.oztechan.ccc.android.core.ad

import android.app.Activity
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

// Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
private val isMobileAdsInitializeCalled = AtomicBoolean(false)

fun Activity.initAds() {
    Logger.v { "Ads initAds" }

    val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(this)
    val params = ConsentRequestParameters.Builder().build()

    consentInformation.requestConsentInfoUpdate(
        this,
        params,
        {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) { loadAndShowError ->
                if (loadAndShowError != null) {
                    Logger.e { "Consent gathering failed: ${loadAndShowError.errorCode}: ${loadAndShowError.message}" }
                }

                // Consent has been gathered.
                if (consentInformation.canRequestAds()) {
                    initializeMobileAdsSdk()
                }
            }
        },
        { Logger.e { "Consent gathering failed: ${it.errorCode}: ${it.message}" } }
    )

    // Check if you can initialize the Google Mobile Ads SDK in parallel
    // while checking for new consent information. Consent obtained in
    // the previous session can be used to request ads.
    if (consentInformation.canRequestAds()) {
        initializeMobileAdsSdk()
    }
}

private fun Activity.initializeMobileAdsSdk() {
    Logger.v { "Ads initializeMobileAdsSdk" }

    if (isMobileAdsInitializeCalled.getAndSet(true)) {
        Logger.v { "Ads initializeMobileAdsSdk is not called, already called" }
        return
    }

    MobileAds.initialize(this)
    MobileAds.setAppVolume(0.0f)
    MobileAds.setAppMuted(true)
}

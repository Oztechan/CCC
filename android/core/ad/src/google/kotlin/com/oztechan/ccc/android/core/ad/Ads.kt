package com.oztechan.ccc.android.core.ad

import android.content.Context
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.MobileAds

fun Context.initAds() {
    Logger.v { "Ads initAds" }
    MobileAds.initialize(this)
    MobileAds.setAppVolume(0.0f)
    MobileAds.setAppMuted(true)
}

package com.oztechan.ccc.android.core.ad

import android.content.Context
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.MobileAds

fun initAds(context: Context) {
    Logger.v { "Ads initAds" }
    MobileAds.initialize(context)
}

package com.github.mustafaozhan.ad

import android.content.Context
import co.touchlab.kermit.Logger
import com.google.android.gms.ads.MobileAds

fun initAds(context: Context) {
    Logger.i { "Ads initAds" }
    MobileAds.initialize(context)
}

package com.oztechan.ccc.android.core.ad

import android.content.Context
import co.touchlab.kermit.Logger
import com.huawei.hms.ads.HwAds

fun initAds(context: Context) {
    Logger.i { "Ads initAds" }
    HwAds.init(context)
}

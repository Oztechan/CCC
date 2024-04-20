package com.oztechan.ccc.android.core.ad

import android.app.Activity
import co.touchlab.kermit.Logger
import com.huawei.hms.ads.HwAds

fun Activity.initAds() {
    Logger.v { "Ads initAds" }
    HwAds.init(this)
    HwAds.setVideoVolume(0f)
    HwAds.setVideoMuted(true)
}

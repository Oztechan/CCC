package com.oztechan.ccc.analytics.util

import android.content.Context
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.huawei.hms.api.HuaweiApiAvailability
import com.scottyab.rootbeer.RootBeer
import com.google.android.gms.common.ConnectionResult as GoogleConnectionResult
import com.huawei.hms.api.ConnectionResult as HuaweiConnectionResult

private const val GOOGLE_MOBILE_SERVICES = "gms"
private const val HUAWEI_MOBILE_SERVICES = "hms"

fun getAvailableServices(context: Context): String {
    val playServiceAvailable = GoogleApiAvailabilityLight.getInstance()
        .isGooglePlayServicesAvailable(context) == GoogleConnectionResult.SUCCESS

    val huaweiServiceAvailable = HuaweiApiAvailability.getInstance()
        .isHuaweiMobileServicesAvailable(context) == HuaweiConnectionResult.SUCCESS

    return when {
        playServiceAvailable && huaweiServiceAvailable -> "$GOOGLE_MOBILE_SERVICES,$HUAWEI_MOBILE_SERVICES"
        playServiceAvailable -> GOOGLE_MOBILE_SERVICES
        huaweiServiceAvailable -> HUAWEI_MOBILE_SERVICES
        else -> ""
    }
}

fun isDeviceRooted(context: Context) = if (RootBeer(context).isRooted) {
    true.toString()
} else {
    false.toString()
}

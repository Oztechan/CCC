package com.mustafaozhan.github.analytics.util

import android.content.Context
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.scottyab.rootbeer.RootBeer
import com.google.android.gms.common.ConnectionResult as GoogleConnectionResult

private const val GOOGLE_MOBILE_SERVICES = "gms"

// todo https://github.com/Oztechan/CCC/issues/303
// private const val HUAWEI_MOBILE_SERVICES = "hms"

fun getAvailableServices(context: Context): String {
    val playServiceAvailable = GoogleApiAvailabilityLight.getInstance()
        .isGooglePlayServicesAvailable(context) == GoogleConnectionResult.SUCCESS

    return if (playServiceAvailable) {
        GOOGLE_MOBILE_SERVICES
    } else {
        ""
    }
//  todo https://github.com/Oztechan/CCC/issues/303
//    val huaweiServiceAvailable = HuaweiApiAvailability.getInstance()
//        .isHuaweiMobileServicesAvailable(context) == HuaweiConnectionResult.SUCCESS
//    return when {
//        playServiceAvailable && huaweiServiceAvailable -> "$GOOGLE_MOBILE_SERVICES,$HUAWEI_MOBILE_SERVICES"
//        playServiceAvailable -> GOOGLE_MOBILE_SERVICES
//        huaweiServiceAvailable -> HUAWEI_MOBILE_SERVICES
//        else -> ""
//    }
}

fun isDeviceRooted(context: Context) = if (RootBeer(context).isRooted) {
    true.toString()
} else {
    false.toString()
}

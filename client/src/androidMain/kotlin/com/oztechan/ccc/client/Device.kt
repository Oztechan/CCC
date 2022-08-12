package com.oztechan.ccc.client

import android.os.Build
import com.oztechan.ccc.client.model.Device

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

actual val device: Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.ANDROID.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.ANDROID.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}

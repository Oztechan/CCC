package com.oztechan.ccc.client

import com.oztechan.ccc.client.model.Device

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

actual val device: Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.ANDROID.GOOGLE
    FLAVOR_HUAWEI -> Device.ANDROID.HUAWEI
    else -> throw IllegalStateException("Invalid device type")
}

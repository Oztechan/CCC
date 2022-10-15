package com.oztechan.ccc.client.di.module.submodule

import android.os.Build
import com.oztechan.ccc.client.BuildConfig
import com.oztechan.ccc.client.model.Device

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

internal actual fun provideDevice(): Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.Android.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.Android.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}

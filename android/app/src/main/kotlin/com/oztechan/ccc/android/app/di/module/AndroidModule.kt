package com.oztechan.ccc.android.app.di.module

import android.content.Context
import android.os.Build
import com.oztechan.ccc.android.app.BuildConfig
import com.oztechan.ccc.client.model.Device
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.dsl.module

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

fun KoinApplication.getAndroidModule(context: Context) = module {
    androidContext(context)
    single { provideDevice() }
}

private fun provideDevice(): Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.Android.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.Android.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}

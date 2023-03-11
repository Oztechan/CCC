/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.app

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import co.touchlab.kermit.Logger
import com.github.submob.logmob.ANRWatchDogHandler
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger
import com.oztechan.ccc.android.app.di.initKoin
import com.oztechan.ccc.android.core.ad.initAds
import com.oztechan.ccc.client.core.analytics.initAnalytics

@Suppress("unused")
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            initCrashlytics()
            initAnalytics(this)
        }

        initLogger()

        Logger.i { "Application onCreate" }

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Thread.setDefaultUncaughtExceptionHandler(ANRWatchDogHandler())
        }

        initAds(this)

        initKoin(this)
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}

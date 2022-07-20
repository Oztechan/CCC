/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import co.touchlab.kermit.Logger
import com.github.submob.logmob.ANRWatchDogHandler
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger
import com.oztechan.ccc.ad.initAds
import com.oztechan.ccc.analytics.initAnalytics
import com.oztechan.ccc.android.di.appModule
import com.oztechan.ccc.client.di.initAndroid
import mustafaozhan.github.com.mycurrencies.BuildConfig

@Suppress("unused")
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        initLogger()

        Logger.i { "Application onCreate" }

        if (!BuildConfig.DEBUG) {
            initAnalytics(this)
            initCrashlytics()
        }

        initAds(this)

        initAndroid(
            context = this,
            appModule = appModule
        )

        Thread.setDefaultUncaughtExceptionHandler(ANRWatchDogHandler())
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

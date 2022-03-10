/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.logmob.ANRWatchDogHandler
import com.github.mustafaozhan.logmob.initCrashlytics
import com.github.mustafaozhan.logmob.initLogger
import com.oztechan.ccc.ad.initAds
import com.oztechan.ccc.analytics.initAnalytics
import com.oztechan.ccc.android.di.platformModule
import com.oztechan.ccc.client.di.initAndroid
import com.oztechan.ccc.config.ConfigManager
import mustafaozhan.github.com.mycurrencies.BuildConfig
import org.koin.android.ext.android.inject

@Suppress("unused")
class Application : Application() {

    private val configManager: ConfigManager by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        initLogger()

        Logger.i { "Application onCreate" }

        if (!BuildConfig.DEBUG) {
            initAnalytics(this)
        }

        initCrashlytics()

        initAds(this)

        initAndroid(
            context = this,
            platformModule = platformModule
        )

        Thread.setDefaultUncaughtExceptionHandler(
            ANRWatchDogHandler(configManager.appConfig.timeOutANRWatchDog)
        )
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

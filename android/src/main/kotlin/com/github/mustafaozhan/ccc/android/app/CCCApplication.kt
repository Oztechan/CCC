/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.android.di.platformModule
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.ANRWatchDogHandler
import com.github.mustafaozhan.logmob.initCrashlytics
import com.github.mustafaozhan.logmob.initLogger
import com.mustafaozhan.github.analytics.initAnalytics
import com.oztechan.ccc.ad.initAds
import com.oztechan.ccc.config.ConfigManager
import mustafaozhan.github.com.mycurrencies.BuildConfig
import org.koin.android.ext.android.inject

@Suppress("unused")
class CCCApplication : Application() {

    private val configManager: ConfigManager by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        initLogger()

        Logger.i { "CCCApplication onCreate" }

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

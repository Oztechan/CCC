/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ad.initAds
import com.github.mustafaozhan.ccc.android.di.platformModule
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initCrashlytics
import com.github.mustafaozhan.logmob.initLogger
import mustafaozhan.github.com.mycurrencies.BuildConfig

@Suppress("unused")
class CCCApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        initLogger().let {
            it.i { "Logger initialized" }
        }

        Logger.i { "CCCApplication onCreate" }

        initCrashlytics(
            context = this,
            enableAnalytics = true,
            enableAnrWatchDog = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
        )

        initAds(this)

        initAndroid(
            context = this,
            platformModule = platformModule
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

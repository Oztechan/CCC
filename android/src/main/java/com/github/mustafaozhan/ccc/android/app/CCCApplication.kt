/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import com.github.mustafaozhan.ccc.android.di.getPlatformModule
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initCrashlytics
import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.logmob.kermit

@Suppress("unused")
class CCCApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initAndroid(
            context = this,
            platformModule = getPlatformModule(this)
        )
        initCrashlytics(this, enableAnalytics = true)
        kermit.d { "CCCApplication onCreate" }
    }
}

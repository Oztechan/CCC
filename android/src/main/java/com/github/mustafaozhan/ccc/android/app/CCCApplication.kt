/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.logmob.initLogMob

@Suppress("unused")
class CCCApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        kermit.d { "CCCApplication onCreate" }
        initAndroid(this@CCCApplication)
        initLogMob(this, enableAnalytics = true)
    }
}

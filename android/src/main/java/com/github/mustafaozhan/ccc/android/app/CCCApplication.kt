/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import android.content.Context
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.dsl.module

@Suppress("unused")
class CCCApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initAndroid(
            module {
                single<Context> { this@CCCApplication }
            }
        )

        initLogMob(this, enableAnalytics = true)
    }
}

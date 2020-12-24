/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.dsl.module

@Suppress("unused")
class CCCApplication : Application() {
    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
    }

    override fun onCreate() {
        super.onCreate()

        initAndroid(
            module {
                single<Context> { this@CCCApplication }
                single<SharedPreferences> {
                    this@CCCApplication.getSharedPreferences(
                        KEY_APPLICATION_PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                }
            }
        )
        initLogMob(this, enableAnalytics = true)
    }
}

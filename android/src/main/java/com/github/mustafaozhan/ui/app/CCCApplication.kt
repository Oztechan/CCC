/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.app

import androidx.multidex.MultiDexApplication
import com.github.mustafaozhan.logmob.initLogMob
import com.github.mustafaozhan.ui.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CCCApplication : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .inject(this)

        initLogMob(
            this,
            enableCrashlytics = true,
            enableAnalytics = true
        )
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}

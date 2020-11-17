/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import androidx.multidex.MultiDexApplication
import com.github.mustafaozhan.ccc.android.di.DaggerApplicationComponent
import com.github.mustafaozhan.ccc.common.firebase.initFirebase
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

        initFirebase(this, true)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}

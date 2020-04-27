// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.app

import androidx.multidex.MultiDexApplication
import com.github.mustafaozhan.logmob.initLogMob
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import mustafaozhan.github.com.mycurrencies.di.DaggerAppComponent
import javax.inject.Inject

class CCCApplication : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
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

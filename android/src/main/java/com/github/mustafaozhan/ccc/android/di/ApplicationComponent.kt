/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ccc.android.app.CCCApplication
import com.github.mustafaozhan.data.di.AppDatabaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@ActivityScope
@FragmentScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityInjectionModule::class,
        FragmentInjectionModule::class,
        ViewModelModule::class,
        AppDatabaseModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: CCCApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: CCCApplication)
}

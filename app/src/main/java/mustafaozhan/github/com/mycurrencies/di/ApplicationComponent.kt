/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import mustafaozhan.github.com.data.di.AppDatabaseModule
import mustafaozhan.github.com.mycurrencies.CCCApplication
import mustafaozhan.github.com.ui.di.ActivityInjectionModule
import mustafaozhan.github.com.ui.di.ActivityScope
import mustafaozhan.github.com.ui.di.FragmentInjectionModule
import mustafaozhan.github.com.ui.di.FragmentScope
import mustafaozhan.github.com.ui.di.ViewModelModule
import javax.inject.Singleton

@Singleton
@ActivityScope
@FragmentScope
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityInjectionModule::class,
    FragmentInjectionModule::class,
    ViewModelModule::class,
    AppDatabaseModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: CCCApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: CCCApplication)
}

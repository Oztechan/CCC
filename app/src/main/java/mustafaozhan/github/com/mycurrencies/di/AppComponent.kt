/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import javax.inject.Singleton

@Singleton
@ActivityScope
@FragmentScope
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityInjectionModule::class,
    FragmentInjectionModule::class,
    ViewModelModule::class,
    ApplicationModule::class,
    AppDatabaseModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: CCCApplication): Builder

        fun build(): AppComponent
    }

    fun inject(app: CCCApplication)
}

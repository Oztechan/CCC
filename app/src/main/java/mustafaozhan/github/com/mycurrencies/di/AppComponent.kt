/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.di.scope.ActivityScope
import mustafaozhan.github.com.mycurrencies.di.scope.FragmentScope
import javax.inject.Singleton

@SuppressWarnings("unchecked")
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

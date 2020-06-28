/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import com.github.mustafaozhan.data.di.AppDatabaseModule
import com.github.mustafaozhan.ui.di.ActivityInjectionModule
import com.github.mustafaozhan.ui.di.ActivityScope
import com.github.mustafaozhan.ui.di.FragmentInjectionModule
import com.github.mustafaozhan.ui.di.FragmentScope
import com.github.mustafaozhan.ui.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import mustafaozhan.github.com.mycurrencies.CCCApplication
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

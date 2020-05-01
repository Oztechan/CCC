/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun providesApplication(application: CCCApplication): Application = application

    @Provides
    @Singleton
    internal fun providesContext(application: CCCApplication): Context = application.applicationContext
}

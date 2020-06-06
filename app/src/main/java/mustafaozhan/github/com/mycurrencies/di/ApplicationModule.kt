/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    @ApplicationContext
    internal fun providesContext(application: CCCApplication): Context = application.applicationContext
}

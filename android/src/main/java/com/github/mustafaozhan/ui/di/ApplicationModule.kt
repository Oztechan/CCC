/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.di

import android.content.Context
import com.github.mustafaozhan.ui.app.CCCApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun providesContext(application: CCCApplication): Context = application.applicationContext
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.di

import android.content.Context
import com.github.mustafaozhan.ccc.android.app.CCCApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun providesContext(application: CCCApplication): Context =
        application.applicationContext
}

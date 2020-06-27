/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.data.api.ApiRepository
import mustafaozhan.github.com.data.preferences.PreferencesRepository
import mustafaozhan.github.com.data.remote.RemoteConfigRepository
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    internal fun providesApiRepository(context: Context) = ApiRepository(context)

    @Provides
    @Singleton
    internal fun providesPreferencesRepository(context: Context) = PreferencesRepository(context)

    @Provides
    @Singleton
    internal fun providesRemoteConfigRepository() = RemoteConfigRepository()
}

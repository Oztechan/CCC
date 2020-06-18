/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.db.AppDatabase
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Provides
    @Singleton
    internal fun providesCurrencyDao(database: AppDatabase) =
        database.currencyDao()

    @Provides
    @Singleton
    internal fun providesOfflineRatesDao(database: AppDatabase) =
        database.offlineRatesDao()

    @Provides
    @Singleton
    internal fun providesAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = AppDatabase.getAppDatabase(applicationContext)
}

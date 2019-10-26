package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.room.AppDatabase
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    internal fun providesAppDatabase(): AppDatabase = AppDatabase.database

    @Provides
    @Singleton
    internal fun providesCurrencyDao(database: AppDatabase) =
        database.currencyDao()

    @Provides
    @Singleton
    internal fun providesOfflineRatesDao(database: AppDatabase) =
        database.offlineRatesDao()
}

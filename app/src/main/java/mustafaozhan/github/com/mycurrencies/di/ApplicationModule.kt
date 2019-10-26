package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.room.AppExecutors
import mustafaozhan.github.com.mycurrencies.room.DiskIOThreadExecutor
import mustafaozhan.github.com.mycurrencies.room.MainThreadExecutor
import java.util.concurrent.Executors
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

    @Singleton
    @Provides
    internal fun providesAppExecutors() =
        AppExecutors(
            DiskIOThreadExecutor(),
            Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
            MainThreadExecutor()
        )
}

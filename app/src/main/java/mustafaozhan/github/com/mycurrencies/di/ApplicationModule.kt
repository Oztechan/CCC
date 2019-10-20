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
    internal fun provideAppDatabase(): AppDatabase = AppDatabase.database

    @Provides
    @Singleton
    internal fun currencyDao(database: AppDatabase) = database.currencyDao()

    @Provides
    @Singleton
    internal fun offlineRatesDao(database: AppDatabase) = database.offlineRatesDao()

    @Singleton
    @Provides
    fun provideAppExecutors() =
        AppExecutors(
            DiskIOThreadExecutor(),
            Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
            MainThreadExecutor()
        )
}

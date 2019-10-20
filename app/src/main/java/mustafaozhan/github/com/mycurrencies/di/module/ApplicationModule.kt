package mustafaozhan.github.com.mycurrencies.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.annotation.ApplicationContext
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.room.AppExecutors
import mustafaozhan.github.com.mycurrencies.room.AppExecutors.Companion.THREAD_COUNT
import mustafaozhan.github.com.mycurrencies.room.DiskIOThreadExecutor
import mustafaozhan.github.com.mycurrencies.room.MainThreadExecutor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:48 PM on Arch Linux wit Love <3.
 */
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return application.applicationContext
    }

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
            Executors.newFixedThreadPool(THREAD_COUNT),
            MainThreadExecutor()
        )
}

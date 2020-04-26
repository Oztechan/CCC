package mustafaozhan.github.com.mycurrencies.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.data.room.AppDatabase
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun providesApplication(application: CCCApplication): Application = application

    @Provides
    @Singleton
    internal fun providesContext(application: CCCApplication): Context =
        application.applicationContext

    @Provides
    @Singleton
    internal fun providesAppDatabase(applicationContext: Context): AppDatabase = Room
        .databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .addMigrations(AppDatabase.FROM_1_TO_2)
        .addMigrations(AppDatabase.FROM_2_TO_3)
        .allowMainThreadQueries().build()

    @Provides
    @Singleton
    internal fun providesCurrencyDao(database: AppDatabase) =
        database.currencyDao()

    @Provides
    @Singleton
    internal fun providesOfflineRatesDao(database: AppDatabase) =
        database.offlineRatesDao()
}

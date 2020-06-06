/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.room.AppDatabase
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL1To2
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL2To3
import javax.inject.Singleton

@Module
@Suppress("MagicNumber")
class AppDatabaseModule {

    companion object {
        private const val DATABASE_NAME = "application_database"
    }

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
    ): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
    }).addMigrations(object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
    }).build()
}

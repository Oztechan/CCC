/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mustafaozhan.github.com.mycurrencies.di.ApplicationContext
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL1To2
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL2To3

@Suppress("MagicNumber")
@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "application_database.sqlite"

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
        }
        private val MIGRATION_2_TO_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
        }

        internal fun createAppDatabase(
            @ApplicationContext context: Context
        ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_NAME)
            .addMigrations(MIGRATION_1_TO_2, MIGRATION_2_TO_3)
            .build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

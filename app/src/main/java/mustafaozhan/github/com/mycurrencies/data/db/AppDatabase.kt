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
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL1To2
import mustafaozhan.github.com.mycurrencies.util.extension.execSQL2To3

@Suppress("MagicNumber")
@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "application_database.sqlite"

        internal fun getAppDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
        }).addMigrations(object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
        }).createFromAsset(DATABASE_NAME)
            .build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

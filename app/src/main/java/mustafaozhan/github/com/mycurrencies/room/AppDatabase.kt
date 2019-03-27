package mustafaozhan.github.com.mycurrencies.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.extensions.execSQL1To2
import mustafaozhan.github.com.mycurrencies.extensions.execSQL2To3
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.room.model.OfflineRates

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Database(entities = [(Currency::class), (OfflineRates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val FROM_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL1To2()
            }
        }
        private val FROM_2_TO_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL2To3()
            }
        }
        val database = Room.databaseBuilder(Application.instance.applicationContext, AppDatabase::class.java, "app_db")
            .addMigrations(FROM_1_TO_2).addMigrations(FROM_2_TO_3).allowMainThreadQueries().build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}
package mustafaozhan.github.com.mycurrencies.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.extension.execSQL1To2
import mustafaozhan.github.com.mycurrencies.extension.execSQL2To3
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Suppress("MagicNumber")
@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        internal const val DATABASE_NAME = "application_database"

        internal val FROM_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
        }
        internal val FROM_2_TO_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
        }
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

package mustafaozhan.github.com.mycurrencies.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.extension.execSQL1To2
import mustafaozhan.github.com.mycurrencies.extension.execSQL2To3
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Suppress("MagicNumber")
@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val FROM_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL1To2()
        }
        private val FROM_2_TO_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) = database.execSQL2To3()
        }

        val database = Room
            .databaseBuilder(
                CCCApplication.instance.applicationContext,
                AppDatabase::class.java,
                "application_database"
            )
            .addMigrations(FROM_1_TO_2)
            .addMigrations(FROM_2_TO_3)
            .allowMainThreadQueries().build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

package mustafaozhan.github.com.mycurrencies.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import mustafaozhan.github.com.mycurrencies.CCCApplication
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Database(entities = [(Currency::class), (Rates::class)], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val FROM_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("INSERT INTO currency (name,longName,symbol,rate,isActive)" +
                        " VALUES ('VES','Venezuelan bolívar soberano','Bs.',0.0,0)")
                    execSQL("ALTER TABLE offline_rates ADD COLUMN VES REAL DEFAULT 0.0")
                }
            }
        }
        val database = Room
            .databaseBuilder(
                CCCApplication.instance.applicationContext,
                AppDatabase::class.java,
                "application_database"
            ).addMigrations(FROM_1_TO_2)
            .allowMainThreadQueries().build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

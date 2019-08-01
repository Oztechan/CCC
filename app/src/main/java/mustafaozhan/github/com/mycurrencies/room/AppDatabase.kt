package mustafaozhan.github.com.mycurrencies.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Database(entities = [(Currency::class), (Rates::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        val database = Room
            .databaseBuilder(
                Application.instance.applicationContext,
                AppDatabase::class.java,
                "application_database"
            ).allowMainThreadQueries().build()
    }

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}
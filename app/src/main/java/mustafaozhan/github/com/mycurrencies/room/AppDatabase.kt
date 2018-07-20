package mustafaozhan.github.com.mycurrencies.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.room.model.OfflineRates

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Database(entities = [(Currency::class), (OfflineRates::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao

}
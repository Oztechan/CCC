package mustafaozhan.github.com.mycurrencies.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Database(entities = [(Currency::class), (Rates::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun offlineRatesDao(): OfflineRatesDao
}

package mustafaozhan.github.com.mycurrencies.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mustafaozhan.github.com.mycurrencies.room.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Dao
abstract class CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCurrency(currency: Currency)

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name")
    abstract fun updateCurrencyStateByName(name: String, isActive: Int)

    @Query("SELECT * FROM currency")
    abstract fun getAllCurrencies(): MutableList<Currency>

    @Query("SELECT * FROM currency WHERE isActive=1")
    abstract fun getActiveCurrencies(): MutableList<Currency>?

    @Query("UPDATE currency set isActive=:value")
    abstract fun updateAllCurrencyState(value: Int)

    @Query("SELECT * FROM currency WHERE name=:name")
    abstract fun getCurrencyByName(name: String): Currency?
}
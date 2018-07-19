package mustafaozhan.github.com.mycurrencies.room.dao

import android.arch.persistence.room.*
import mustafaozhan.github.com.mycurrencies.room.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Dao
abstract class CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCurrency(currency: Currency)

    @Update
    abstract fun updateCurrency(currency: Currency)

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name ")
    abstract fun updateCurrencyActivityByName(name: String, isActive: Int)


    @Query("SELECT * FROM currency")
    abstract fun getAllCurrencies(): MutableList<Currency>

    @Query("SELECT * FROM currency WHERE isActive=1")
    abstract fun getActiveCurrencies(): MutableList<Currency>


}
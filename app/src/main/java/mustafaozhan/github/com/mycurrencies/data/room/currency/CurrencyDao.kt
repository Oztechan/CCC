package mustafaozhan.github.com.mycurrencies.data.room.currency

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mustafaozhan.github.com.mycurrencies.model.Currency

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name")
    fun updateCurrencyStateByName(name: String, isActive: Int)

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): MutableList<Currency>

    @Query("SELECT * FROM currency WHERE isActive=1")
    fun getActiveCurrencies(): LiveData<MutableList<Currency>?>

    @Query("UPDATE currency set isActive=:value")
    fun updateAllCurrencyState(value: Int)

    @Query("SELECT * FROM currency WHERE name=:name")
    fun getCurrencyByName(name: String): Currency?
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.room.currency

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mustafaozhan.github.com.mycurrencies.model.Currency

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name")
    fun updateCurrencyStateByName(name: String, isActive: Boolean)

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): LiveData<MutableList<Currency>>

    @Query("SELECT * FROM currency WHERE isActive=1")
    fun getActiveCurrencies(): LiveData<MutableList<Currency>?>

    @Query("UPDATE currency set isActive=:value")
    fun updateAllCurrencyState(value: Boolean)

    @Query("SELECT * FROM currency WHERE name=:name")
    fun getCurrencyByName(name: String): Currency?
}

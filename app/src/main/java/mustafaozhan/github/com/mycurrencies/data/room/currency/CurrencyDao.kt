/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.room.currency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mustafaozhan.github.com.mycurrencies.model.Currency

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): Flow<MutableList<Currency>>

    @Query("SELECT * FROM currency WHERE isActive=1")
    fun getActiveCurrencies(): Flow<MutableList<Currency>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name")
    suspend fun updateCurrencyStateByName(name: String, isActive: Boolean)

    @Query("UPDATE currency set isActive=:value")
    suspend fun updateAllCurrencyState(value: Boolean)

    @Query("SELECT * FROM currency WHERE name=:name")
    suspend fun getCurrencyByName(name: String): Currency?
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.db

import androidx.room.Dao
import androidx.room.Query
import com.github.mustafaozhan.data.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): Flow<MutableList<Currency>>

    @Query("SELECT * FROM currency WHERE isActive=1")
    fun getActiveCurrencies(): Flow<MutableList<Currency>?>

    @Query("UPDATE currency set isActive=:isActive WHERE name=:name")
    suspend fun updateCurrencyStateByName(name: String, isActive: Boolean)

    @Query("UPDATE currency set isActive=:value")
    suspend fun updateAllCurrencyState(value: Boolean)

    @Query("SELECT * FROM currency WHERE name=:name")
    suspend fun getCurrencyByName(name: String): Currency?
}

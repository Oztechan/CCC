package com.oztechan.ccc.common.datasource.currency

import com.oztechan.ccc.common.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun collectAllCurrencies(): Flow<List<Currency>>

    fun collectActiveCurrencies(): Flow<List<Currency>>

    suspend fun getActiveCurrencies(): List<Currency>

    suspend fun updateCurrencyStateByName(name: String, isActive: Boolean)

    suspend fun updateAllCurrencyState(value: Boolean)

    suspend fun getCurrencyByName(name: String): Currency?
}

package com.oztechan.ccc.common.datasource.currency

import com.oztechan.ccc.common.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun getCurrenciesFlow(): Flow<List<Currency>>

    fun getActiveCurrenciesFlow(): Flow<List<Currency>>

    suspend fun getActiveCurrencies(): List<Currency>

    suspend fun updateCurrencyStateByCode(code: String, isActive: Boolean)

    suspend fun updateCurrencyStates(value: Boolean)

    suspend fun getCurrencyByCode(code: String): Currency?
}

package com.oztechan.ccc.common.datasource.currency

import com.oztechan.ccc.common.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun collectAllCurrencies(): Flow<List<Currency>>

    fun collectActiveCurrencies(): Flow<List<Currency>>

    fun getActiveCurrencies(): List<Currency>

    fun updateCurrencyStateByName(name: String, isActive: Boolean)

    fun updateAllCurrencyState(value: Boolean)

    fun getCurrencyByName(name: String): Currency?
}

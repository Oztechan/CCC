package com.github.mustafaozhan.ccc.common.db.currency

import com.github.mustafaozhan.ccc.common.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun collectAllCurrencies(): Flow<List<Currency>>

    fun collectActiveCurrencies(): Flow<List<Currency>>

    fun getActiveCurrencies(): List<Currency>

    fun updateCurrencyStateByName(name: String, isActive: Boolean)

    fun updateAllCurrencyState(value: Boolean)

    fun getCurrencyByName(name: String): Currency?
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.db

import com.github.mustafaozhan.ccc.common.CurrencyQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class CurrencyDao(private val currencyQueries: CurrencyQueries) {

    fun collectAllCurrencies() = currencyQueries.collectAllCurrencies().asFlow().mapToList()

    fun collectActiveCurrencies() =
        currencyQueries.collectActiveCurrencies().asFlow().mapToList()

    fun getActiveCurrencies() = currencyQueries.getActiveCurrencies().executeAsList()

    fun updateCurrencyStateByName(name: String, isActive: Boolean) =
        currencyQueries.updateCurrencyStateByName(if (isActive) 1 else 0, name)

    fun updateAllCurrencyState(value: Boolean) =
        currencyQueries.updateAllCurrencyState(if (value) 1 else 0)

    fun getCurrencyByName(name: String) =
        currencyQueries.getCurrencyByName(name).executeAsOneOrNull()
}

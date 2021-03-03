/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.db

import com.github.mustafaozhan.ccc.common.sql.CurrencyQueries
import com.github.mustafaozhan.logmob.kermit
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

class CurrencyDao(private val currencyQueries: CurrencyQueries) {

    fun collectAllCurrencies() = currencyQueries
        .collectAllCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .also { kermit.d { "CurrencyDao collectAllCurrencies" } }

    fun collectActiveCurrencies() = currencyQueries
        .collectActiveCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .also { kermit.d { "CurrencyDao collectActiveCurrencies" } }

    fun getActiveCurrencies() = currencyQueries
        .getActiveCurrencies()
        .executeAsList()
        .also { kermit.d { "CurrencyDao getActiveCurrencies" } }

    fun updateCurrencyStateByName(name: String, isActive: Boolean) = currencyQueries
        .updateCurrencyStateByName(if (isActive) 1 else 0, name)
        .also { kermit.d { "CurrencyDao updateCurrencyStateByName $name $isActive" } }

    fun updateAllCurrencyState(value: Boolean) = currencyQueries
        .updateAllCurrencyState(if (value) 1 else 0)
        .also { kermit.d { "CurrencyDao updateAllCurrencyState $value" } }

    fun getCurrencyByName(name: String) = currencyQueries
        .getCurrencyByName(name)
        .executeAsOneOrNull()
        .also { kermit.d { "CurrencyDao getCurrencyByName $name" } }
}

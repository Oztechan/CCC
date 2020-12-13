/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.db

import com.github.mustafaozhan.ccc.common.CurrencyQueries
import com.github.mustafaozhan.ccc.common.model.mapToModel
import com.github.mustafaozhan.ccc.common.model.toModel
import com.github.mustafaozhan.ccc.common.model.toModelList
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class CurrencyDao(private val currencyQueries: CurrencyQueries) {

    fun collectAllCurrencies() =
        currencyQueries.collectAllCurrencies().asFlow().mapToList().mapToModel()

    fun collectActiveCurrencies() =
        currencyQueries.collectActiveCurrencies().asFlow().mapToList().mapToModel()

    fun getActiveCurrencies() =
        currencyQueries.getActiveCurrencies().executeAsList().toModelList()

    fun updateCurrencyStateByName(name: String, isActive: Boolean) =
        currencyQueries.updateCurrencyStateByName(if (isActive) 1 else 0, name)

    fun updateAllCurrencyState(value: Boolean) =
        currencyQueries.updateAllCurrencyState(if (value) 1 else 0)

    fun getCurrencyByName(name: String) =
        currencyQueries.getCurrencyByName(name).executeAsOneOrNull()?.toModel()
}

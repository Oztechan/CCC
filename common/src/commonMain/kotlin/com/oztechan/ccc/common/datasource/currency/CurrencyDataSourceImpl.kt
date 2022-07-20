package com.oztechan.ccc.common.datasource.currency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toModelList
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

internal class CurrencyDataSourceImpl(
    private val currencyQueries: CurrencyQueries
) : CurrencyDataSource {

    override fun collectAllCurrencies() = currencyQueries
        .collectAllCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .mapToModel()
        .also { Logger.v { "CurrencyDataSourceImpl collectAllCurrencies" } }

    override fun collectActiveCurrencies() = currencyQueries
        .collectActiveCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .mapToModel()
        .also { Logger.v { "CurrencyDataSourceImpl collectActiveCurrencies" } }

    override fun getActiveCurrencies() = currencyQueries
        .getActiveCurrencies()
        .executeAsList()
        .toModelList()
        .also { Logger.v { "CurrencyDataSourceImpl getActiveCurrencies" } }

    override fun updateCurrencyStateByName(name: String, isActive: Boolean) = currencyQueries
        .updateCurrencyStateByName(isActive.toLong(), name)
        .also { Logger.v { "CurrencyDataSourceImpl updateCurrencyStateByName $name $isActive" } }

    override fun updateAllCurrencyState(value: Boolean) = currencyQueries
        .updateAllCurrencyState(value.toLong())
        .also { Logger.v { "CurrencyDataSourceImpl updateAllCurrencyState $value" } }

    override fun getCurrencyByName(name: String) = currencyQueries
        .getCurrencyByName(name)
        .executeAsOneOrNull()
        ?.toModel()
        .also { Logger.v { "CurrencyDataSourceImpl getCurrencyByName $name" } }
}

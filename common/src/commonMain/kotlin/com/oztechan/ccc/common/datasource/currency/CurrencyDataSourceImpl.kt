package com.oztechan.ccc.common.datasource.currency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.datasource.BaseDataSource
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toModelList
import com.oztechan.ccc.common.model.Currency
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CurrencyDataSourceImpl(
    private val currencyQueries: CurrencyQueries,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyDataSource, BaseDataSource(ioDispatcher) {

    override fun collectAllCurrencies(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl collectAllCurrencies" }
        return currencyQueries.collectAllCurrencies()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.sortedBy { (name) -> name } }
            .mapToModel()
    }

    override fun collectActiveCurrencies(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl collectActiveCurrencies" }
        return currencyQueries.collectActiveCurrencies()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { it.sortedBy { (name) -> name } }
            .mapToModel()
    }

    override suspend fun getActiveCurrencies() = dbQuery {
        Logger.v { "CurrencyDataSourceImpl getActiveCurrencies" }
        currencyQueries.getActiveCurrencies().executeAsList().toModelList()
    }

    override suspend fun updateCurrencyStateByName(name: String, isActive: Boolean) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl updateCurrencyStateByName $name $isActive" }
        currencyQueries.updateCurrencyStateByName(isActive.toLong(), name)
    }

    override suspend fun updateAllCurrencyState(value: Boolean) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl updateAllCurrencyState $value" }
        currencyQueries.updateAllCurrencyState(value.toLong())
    }

    override suspend fun getCurrencyByName(name: String) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl getCurrencyByName $name" }
        currencyQueries.getCurrencyByName(name).executeAsOneOrNull()?.toModel()
    }
}

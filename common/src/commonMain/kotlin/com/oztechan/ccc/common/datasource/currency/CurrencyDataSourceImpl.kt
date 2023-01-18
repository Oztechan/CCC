package com.oztechan.ccc.common.datasource.currency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.database.sql.CurrencyQueries
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.Currency
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CurrencyDataSourceImpl(
    private val currencyQueries: CurrencyQueries,
    private val ioDispatcher: CoroutineDispatcher
) : CurrencyDataSource, BaseDBDataSource(ioDispatcher) {

    override fun getCurrenciesFlow(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl getCurrenciesFlow" }
        return currencyQueries.getCurrencies()
            .toDBFlow()
            .mapToList(ioDispatcher)
            .map { it.sortedBy { (name) -> name } }
            .map { currencyList ->
                currencyList.map { it.toModel() }
            }
    }

    override fun getActiveCurrenciesFlow(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl getActiveCurrenciesFlow" }
        return currencyQueries.getActiveCurrencies()
            .toDBFlow()
            .mapToList(ioDispatcher)
            .map { it.sortedBy { (name) -> name } }
            .map { currencyList ->
                currencyList.map { it.toModel() }
            }
    }

    override suspend fun getActiveCurrencies() = dbQuery {
        Logger.v { "CurrencyDataSourceImpl getActiveCurrencies" }
        currencyQueries.getActiveCurrencies()
            .executeAsList()
            .map { it.toModel() }
    }

    override suspend fun updateCurrencyStateByCode(code: String, isActive: Boolean) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl updateCurrencyStateByCode $code $isActive" }
        currencyQueries.updateCurrencyStateByCode(isActive.toLong(), code)
    }

    override suspend fun updateCurrencyStates(value: Boolean) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl updateCurrencyStates $value" }
        currencyQueries.updateCurrencyStates(value.toLong())
    }

    override suspend fun getCurrencyByCode(code: String) = dbQuery {
        Logger.v { "CurrencyDataSourceImpl getCurrencyByCode $code" }
        currencyQueries.getCurrencyByCode(code).executeAsOneOrNull()?.toModel()
    }
}

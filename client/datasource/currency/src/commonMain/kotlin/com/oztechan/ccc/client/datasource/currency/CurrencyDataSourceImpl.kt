package com.oztechan.ccc.client.datasource.currency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.currency.mapper.toCurrencyModel
import com.oztechan.ccc.common.core.database.base.BaseDBDataSource
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.CurrencyQueries
import com.oztechan.ccc.common.core.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CurrencyDataSourceImpl(
    private val currencyQueries: CurrencyQueries,
    ioDispatcher: CoroutineDispatcher
) : CurrencyDataSource, BaseDBDataSource(ioDispatcher) {

    override fun getCurrenciesFlow(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl getCurrenciesFlow" }
        return currencyQueries.getCurrencies()
            .toDBFlowList()
            .map { it.sortedBy { (name) -> name } }
            .map { currencyList ->
                currencyList.map { it.toCurrencyModel() }
            }
    }

    override fun getActiveCurrenciesFlow(): Flow<List<Currency>> {
        Logger.v { "CurrencyDataSourceImpl getActiveCurrenciesFlow" }
        return currencyQueries.getActiveCurrencies()
            .toDBFlowList()
            .map { it.sortedBy { (name) -> name } }
            .map { currencyList ->
                currencyList.map { it.toCurrencyModel() }
            }
    }

    override suspend fun getActiveCurrencies() = dbQuery {
        Logger.v { "CurrencyDataSourceImpl getActiveCurrencies" }
        currencyQueries.getActiveCurrencies()
            .executeAsList()
            .map { it.toCurrencyModel() }
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
        currencyQueries.getCurrencyByCode(code)
            .executeAsOneOrNull()
            ?.toCurrencyModel()
    }
}

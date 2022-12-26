package com.oztechan.ccc.common.datasource.rates

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.database.sql.RatesQueries
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.mapper.toCurrencyResponseEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toRates
import com.oztechan.ccc.common.mapper.toSerializedString
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import kotlinx.coroutines.CoroutineDispatcher

internal class RatesDataSourceImpl(
    private val ratesQueries: RatesQueries,
    ioDispatcher: CoroutineDispatcher
) : RatesDataSource, BaseDBDataSource(ioDispatcher) {

    override suspend fun insertRates(currencyResponse: CurrencyResponse) = dbQuery {
        Logger.v { "RatesDataSourceImpl insertRates ${currencyResponse.base}" }
        ratesQueries.insertRates(currencyResponse.toRates())
    }

    override suspend fun getRatesByBase(baseName: String): Rates? = dbQuery {
        Logger.v { "RatesDataSourceImpl getRatesByBase $baseName" }
        ratesQueries.getRatesByBase(baseName)
            .executeAsOneOrNull()
            ?.toModel()
    }

    override suspend fun getCurrencyResponseTextByBase(baseName: String) = dbQuery {
        Logger.v { "RatesDataSourceImpl getCurrencyResponseTextByBase $baseName" }
        ratesQueries.getRatesByBase(baseName.uppercase())
            .executeAsOneOrNull()
            ?.toCurrencyResponseEntity()
            ?.toSerializedString()
    }
}

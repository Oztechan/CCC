package com.oztechan.ccc.common.datasource.exchangerate

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.core.database.base.BaseDBDataSource
import com.oztechan.ccc.common.mapper.toExchangeRateAPIModel
import kotlinx.coroutines.CoroutineDispatcher

internal class ExchangeRateDataSourceImpl(
    private val conversionQueries: ConversionQueries,
    ioDispatcher: CoroutineDispatcher
) : ExchangeRateDataSource, BaseDBDataSource(ioDispatcher) {
    override suspend fun getExchangeRateByBase(baseName: String) = dbQuery {
        Logger.v { "ConversionDataSourceImpl getExchangeRateByBase $baseName" }
        conversionQueries.getConversionByBase(baseName.uppercase())
            .executeAsOneOrNull()
            ?.toExchangeRateAPIModel()
    }
}

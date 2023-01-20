package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.mapper.toConversionDBModel
import com.oztechan.ccc.common.mapper.toConversionModel
import com.oztechan.ccc.common.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.mapper.toSerializedString
import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.ExchangeRate
import kotlinx.coroutines.CoroutineDispatcher

internal class ConversionDataSourceImpl(
    private val conversionQueries: ConversionQueries,
    ioDispatcher: CoroutineDispatcher
) : ConversionDataSource, BaseDBDataSource(ioDispatcher) {

    override suspend fun insertConversion(exchangeRate: ExchangeRate) = dbQuery {
        Logger.v { "ConversionDataSourceImpl insertConversion ${exchangeRate.base}" }
        conversionQueries.insertConversion(exchangeRate.toConversionDBModel())
    }

    override suspend fun getConversionByBase(baseName: String): Conversion? = dbQuery {
        Logger.v { "ConversionDataSourceImpl getConversionByBase $baseName" }
        conversionQueries.getConversionByBase(baseName)
            .executeAsOneOrNull()
            ?.toConversionModel()
    }

    override suspend fun getExchangeRateTextByBase(baseName: String) = dbQuery {
        Logger.v { "ConversionDataSourceImpl getExchangeRateTextByBase $baseName" }
        conversionQueries.getConversionByBase(baseName.uppercase())
            .executeAsOneOrNull()
            ?.toExchangeRateAPIModel()
            ?.toSerializedString()
    }
}

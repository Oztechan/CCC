package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.mapper.toConversion
import com.oztechan.ccc.common.mapper.toCurrencyResponseEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toSerializedString
import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.CurrencyResponse
import kotlinx.coroutines.CoroutineDispatcher

internal class ConversionDataSourceImpl(
    private val conversionQueries: ConversionQueries,
    ioDispatcher: CoroutineDispatcher
) : ConversionDataSource, BaseDBDataSource(ioDispatcher) {

    override suspend fun insertConversion(currencyResponse: CurrencyResponse) = dbQuery {
        Logger.v { "ConversionDataSourceImpl insertConversion ${currencyResponse.base}" }
        conversionQueries.insertConversion(currencyResponse.toConversion())
    }

    override suspend fun getConversionByBase(baseName: String): Conversion? = dbQuery {
        Logger.v { "ConversionDataSourceImpl getConversionByBase $baseName" }
        conversionQueries.getConversionByBase(baseName)
            .executeAsOneOrNull()
            ?.toModel()
    }

    override suspend fun getCurrencyResponseTextByBase(baseName: String) = dbQuery {
        Logger.v { "ConversionDataSourceImpl getCurrencyResponseTextByBase $baseName" }
        conversionQueries.getConversionByBase(baseName.uppercase())
            .executeAsOneOrNull()
            ?.toCurrencyResponseEntity()
            ?.toSerializedString()
    }
}

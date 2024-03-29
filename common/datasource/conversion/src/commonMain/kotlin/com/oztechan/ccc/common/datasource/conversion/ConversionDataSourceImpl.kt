package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.base.BaseDBDataSource
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.datasource.conversion.mapper.toConversionDBModel
import com.oztechan.ccc.common.datasource.conversion.mapper.toConversionModel
import kotlinx.coroutines.CoroutineDispatcher

internal class ConversionDataSourceImpl(
    private val conversionQueries: ConversionQueries,
    ioDispatcher: CoroutineDispatcher
) : ConversionDataSource, BaseDBDataSource(ioDispatcher) {

    override suspend fun insertConversion(conversion: Conversion) = dbQuery {
        Logger.v { "ConversionDataSourceImpl insertConversion ${conversion.base}" }
        conversionQueries.insertConversion(conversion.toConversionDBModel())
    }

    override suspend fun getConversionByBase(baseName: String): Conversion? = dbQuery {
        Logger.v { "ConversionDataSourceImpl getConversionByBase $baseName" }
        conversionQueries.getConversionByBase(baseName)
            .executeAsOneOrNull()
            ?.toConversionModel()
    }
}

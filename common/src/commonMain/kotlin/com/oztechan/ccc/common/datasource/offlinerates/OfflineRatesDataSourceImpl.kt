package com.oztechan.ccc.common.datasource.offlinerates

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.mapper.toCurrencyResponseEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
import com.oztechan.ccc.common.mapper.toSerializedString
import com.oztechan.ccc.common.model.CurrencyResponse
import kotlinx.coroutines.CoroutineDispatcher

internal class OfflineRatesDataSourceImpl(
    private val offlineRatesQueries: OfflineRatesQueries,
    ioDispatcher: CoroutineDispatcher
) : OfflineRatesDataSource, BaseDBDataSource(ioDispatcher) {

    override suspend fun insertOfflineRates(currencyResponse: CurrencyResponse) = dbQuery {
        Logger.v { "OfflineRatesDataSourceImpl insertOfflineRates ${currencyResponse.base}" }
        offlineRatesQueries.insertOfflineRates(currencyResponse.toOfflineRates())
    }

    override suspend fun getOfflineRatesByBase(baseName: String) = dbQuery {
        Logger.v { "OfflineRatesDataSourceImpl getOfflineRatesByBase $baseName" }
        offlineRatesQueries.getOfflineRatesByBase(baseName)
            .executeAsOneOrNull()
            ?.toModel()
    }

    override suspend fun getOfflineCurrencyResponseByBase(baseName: String) = dbQuery {
        Logger.v { "OfflineRatesDataSourceImpl getOfflineCurrencyResponseByBase $baseName" }
        offlineRatesQueries.getOfflineRatesByBase(baseName.uppercase())
            .executeAsOneOrNull()
            ?.toCurrencyResponseEntity()
            ?.toSerializedString()
    }
}

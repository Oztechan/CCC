package com.oztechan.ccc.common.db.offlinerates

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.mapper.toCurrencyResponseEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
import com.oztechan.ccc.common.mapper.toSerializedString
import com.oztechan.ccc.common.model.CurrencyResponse

internal class OfflineRatesRepositoryImpl(
    private val offlineRatesQueries: OfflineRatesQueries
) : OfflineRatesRepository {

    override fun insertOfflineRates(currencyResponse: CurrencyResponse) = offlineRatesQueries
        .insertOfflineRates(currencyResponse.toOfflineRates())
        .also { Logger.v { "OfflineRatesRepositoryImpl insertOfflineRates ${currencyResponse.base}" } }

    override fun getOfflineRatesByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName)
        .executeAsOneOrNull()
        ?.toModel()
        .also { Logger.v { "OfflineRatesRepositoryImpl getOfflineRatesByBase $baseName" } }

    override fun getOfflineCurrencyResponseByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName.uppercase())
        .executeAsOneOrNull()
        ?.toCurrencyResponseEntity()
        ?.toSerializedString()
        .also { Logger.v { "OfflineRatesRepositoryImpl getOfflineCurrencyResponseByBase $baseName" } }
}

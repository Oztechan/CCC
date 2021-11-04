package com.github.mustafaozhan.ccc.common.db.offlinerates

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.mapper.toCurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toOfflineRates
import com.github.mustafaozhan.ccc.common.mapper.toSerializedString
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse

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

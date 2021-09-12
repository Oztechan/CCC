package com.github.mustafaozhan.ccc.common.db.offlinerates

import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.mapper.toCurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toOfflineRates
import com.github.mustafaozhan.ccc.common.mapper.toSerializedString
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.logmob.kermit

internal class OfflineRatesRepositoryImpl(
    private val offlineRatesQueries: OfflineRatesQueries
) : OfflineRatesRepository {

    override fun insertOfflineRates(currencyResponse: CurrencyResponse) = offlineRatesQueries
        .insertOfflineRates(currencyResponse.toOfflineRates())
        .also { kermit.d { "OfflineRatesRepositoryImpl insertOfflineRates ${currencyResponse.base}" } }

    override fun getOfflineRatesByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName)
        .executeAsOneOrNull()
        ?.toModel()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineRatesByBase $baseName" } }

    override fun getOfflineCurrencyResponseByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName.uppercase())
        .executeAsOneOrNull()
        ?.toCurrencyResponseEntity()
        ?.toSerializedString()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineCurrencyResponseByBase $baseName" } }
}

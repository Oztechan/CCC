package com.oztechan.ccc.common.datasource.offlinerates

import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates

interface OfflineRatesDataSource {
    suspend fun insertOfflineRates(currencyResponse: CurrencyResponse)

    suspend fun getOfflineRatesByBase(baseName: String): Rates?

    suspend fun getOfflineCurrencyResponseByBase(baseName: String): String?
}

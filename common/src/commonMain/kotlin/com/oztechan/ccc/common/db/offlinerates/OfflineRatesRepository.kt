package com.oztechan.ccc.common.db.offlinerates

import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates

interface OfflineRatesRepository {
    fun insertOfflineRates(currencyResponse: CurrencyResponse)

    fun getOfflineRatesByBase(baseName: String): Rates?

    fun getOfflineCurrencyResponseByBase(baseName: String): String?
}

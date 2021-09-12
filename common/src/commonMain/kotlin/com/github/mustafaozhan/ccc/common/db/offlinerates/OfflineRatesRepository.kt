package com.github.mustafaozhan.ccc.common.db.offlinerates

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates

interface OfflineRatesRepository {
    fun insertOfflineRates(currencyResponse: CurrencyResponse)

    fun getOfflineRatesByBase(baseName: String): Rates?

    fun getOfflineCurrencyResponseByBase(baseName: String): String?
}

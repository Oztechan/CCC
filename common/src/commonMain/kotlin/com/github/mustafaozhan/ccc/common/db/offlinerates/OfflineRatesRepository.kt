package com.github.mustafaozhan.ccc.common.db.offlinerates

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates

interface OfflineRatesRepository {
    fun insertOfflineRates(rates: Rates)

    fun getOfflineRatesByBase(baseName: String): Rates?

    fun getOfflineCurrencyResponseByBase(baseName: String): CurrencyResponse?
}

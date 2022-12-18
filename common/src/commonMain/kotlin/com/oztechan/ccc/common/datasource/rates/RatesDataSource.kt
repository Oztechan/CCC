package com.oztechan.ccc.common.datasource.rates

import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates

interface RatesDataSource {
    suspend fun insertRates(currencyResponse: CurrencyResponse)

    suspend fun getRatesByBase(baseName: String): Rates?

    suspend fun getCurrencyResponseTextByBase(baseName: String): String?
}

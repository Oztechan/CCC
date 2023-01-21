package com.oztechan.ccc.common.datasource.conversion

import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.ExchangeRate

interface ConversionDataSource {
    suspend fun insertConversion(exchangeRate: ExchangeRate)

    suspend fun getConversionByBase(baseName: String): Conversion?

    suspend fun getExchangeRateTextByBase(baseName: String): String?
}

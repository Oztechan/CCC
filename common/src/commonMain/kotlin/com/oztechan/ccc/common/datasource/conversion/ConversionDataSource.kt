package com.oztechan.ccc.common.datasource.conversion

import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.CurrencyResponse

interface ConversionDataSource {
    suspend fun insertConversion(currencyResponse: CurrencyResponse)

    suspend fun getConversionByBase(baseName: String): Conversion?

    suspend fun getCurrencyResponseTextByBase(baseName: String): String?
}

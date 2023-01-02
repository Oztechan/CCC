package com.oztechan.ccc.common.api.free

import com.oztechan.ccc.common.api.model.CurrencyResponse

internal interface FreeApi {
    suspend fun getConversion(base: String): CurrencyResponse
}

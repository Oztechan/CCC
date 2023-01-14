package com.oztechan.ccc.common.api.free

import com.oztechan.ccc.common.api.model.ExchangeRate

internal interface FreeApi {
    suspend fun getConversion(base: String): ExchangeRate
}

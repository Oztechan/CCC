package com.oztechan.ccc.common.api.free

import com.oztechan.ccc.common.api.model.CurrencyResponse

interface FreeApi {
    suspend fun getRates(base: String): CurrencyResponse
}

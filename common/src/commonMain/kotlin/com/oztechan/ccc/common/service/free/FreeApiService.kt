package com.oztechan.ccc.common.service.free

import com.oztechan.ccc.common.model.CurrencyResponse

interface FreeApiService {
    suspend fun getConversion(base: String): CurrencyResponse
}

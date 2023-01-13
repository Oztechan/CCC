package com.oztechan.ccc.common.service.free

import com.oztechan.ccc.common.model.ExchangeRate

interface FreeApiService {
    suspend fun getConversion(base: String): ExchangeRate
}

package com.oztechan.ccc.common.data.service.free

import com.oztechan.ccc.common.core.model.ExchangeRate

interface FreeApiService {
    suspend fun getConversion(base: String): ExchangeRate
}

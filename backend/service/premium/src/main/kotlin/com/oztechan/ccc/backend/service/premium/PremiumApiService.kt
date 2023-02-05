package com.oztechan.ccc.backend.service.premium

import com.oztechan.ccc.common.core.model.ExchangeRate

interface PremiumApiService {
    suspend fun getConversion(base: String): ExchangeRate
}

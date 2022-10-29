package com.oztechan.ccc.common.service.premium

import com.oztechan.ccc.common.model.CurrencyResponse

interface PremiumApiService {
    suspend fun getRates(base: String): CurrencyResponse
}

package com.oztechan.ccc.common.api.service

import com.oztechan.ccc.common.api.model.CurrencyResponse

internal interface ApiService {
    suspend fun getRatesByPremiumAPI(base: String): CurrencyResponse
}

package com.oztechan.ccc.common.api.repo

import com.oztechan.ccc.common.model.CurrencyResponse

interface ApiRepository {
    suspend fun getRatesByBackend(base: String): CurrencyResponse

    suspend fun getRatesByPremiumAPI(base: String): CurrencyResponse
}

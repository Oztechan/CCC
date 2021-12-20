package com.github.mustafaozhan.ccc.common.api.repo

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse

interface ApiRepository {
    suspend fun getRatesByBackend(base: String): CurrencyResponse

    suspend fun getRatesByAPI(base: String): CurrencyResponse

    suspend fun getRatesByPremiumAPI(base: String): CurrencyResponse
}

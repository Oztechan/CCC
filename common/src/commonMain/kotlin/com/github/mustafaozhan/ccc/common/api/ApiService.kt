package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity

internal interface ApiService {
    suspend fun getRatesByBackend(base: String): CurrencyResponseEntity

    suspend fun getRatesByAPI(base: String): CurrencyResponseEntity

    suspend fun getRatesByPremiumAPI(base: String): CurrencyResponseEntity
}

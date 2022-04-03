package com.oztechan.ccc.common.api.service

import com.oztechan.ccc.common.entity.CurrencyResponseEntity

internal interface ApiService {
    suspend fun getRatesByBackend(base: String): CurrencyResponseEntity

    suspend fun getRatesByAPI(base: String): CurrencyResponseEntity

    suspend fun getRatesByPremiumAPI(base: String): CurrencyResponseEntity
}

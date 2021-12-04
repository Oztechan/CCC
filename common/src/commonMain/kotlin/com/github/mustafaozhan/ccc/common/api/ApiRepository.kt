package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.util.Result

interface ApiRepository {
    suspend fun getRatesByBackend(base: String): Result<out CurrencyResponse>

    suspend fun getRatesByAPI(base: String): Result<out CurrencyResponse>

    suspend fun getRatesByPremiumAPI(base: String): Result<out CurrencyResponse>
}

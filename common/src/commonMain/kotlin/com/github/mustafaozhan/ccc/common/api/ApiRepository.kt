package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.util.Result

interface ApiRepository {
    suspend fun getRatesViaBackend(base: String): Result<out CurrencyResponse>

    suspend fun getUnPopularRates(base: String): Result<out CurrencyResponse>

    suspend fun getPopularRates(base: String): Result<out CurrencyResponse>
}

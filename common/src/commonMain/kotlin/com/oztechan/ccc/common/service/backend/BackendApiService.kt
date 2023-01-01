package com.oztechan.ccc.common.service.backend

import com.oztechan.ccc.common.model.CurrencyResponse

interface BackendApiService {
    suspend fun getConversion(base: String): CurrencyResponse
}

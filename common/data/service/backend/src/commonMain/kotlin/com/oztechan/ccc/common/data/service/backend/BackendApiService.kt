package com.oztechan.ccc.common.data.service.backend

import com.oztechan.ccc.common.core.model.ExchangeRate

interface BackendApiService {
    suspend fun getConversion(base: String): ExchangeRate
}

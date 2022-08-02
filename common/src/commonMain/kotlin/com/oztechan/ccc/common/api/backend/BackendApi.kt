package com.oztechan.ccc.common.api.backend

import com.oztechan.ccc.common.api.model.CurrencyResponse

interface BackendApi {
    suspend fun getRates(base: String): CurrencyResponse
}

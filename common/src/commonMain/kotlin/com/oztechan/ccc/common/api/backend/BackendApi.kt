package com.oztechan.ccc.common.api.backend

import com.oztechan.ccc.common.api.model.CurrencyResponse

internal interface BackendApi {
    suspend fun getRates(base: String): CurrencyResponse
}

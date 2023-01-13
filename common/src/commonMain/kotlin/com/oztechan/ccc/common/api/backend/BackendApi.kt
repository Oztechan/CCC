package com.oztechan.ccc.common.api.backend

import com.oztechan.ccc.common.api.model.ExchangeRate

internal interface BackendApi {
    suspend fun getConversion(base: String): ExchangeRate
}

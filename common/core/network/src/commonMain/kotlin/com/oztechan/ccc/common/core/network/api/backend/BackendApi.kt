package com.oztechan.ccc.common.core.network.api.backend

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface BackendApi {
    suspend fun getConversion(base: String): ExchangeRate
}

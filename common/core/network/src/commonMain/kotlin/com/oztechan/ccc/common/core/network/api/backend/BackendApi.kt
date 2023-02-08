package com.oztechan.ccc.common.core.network.api.backend

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface BackendApi {
    suspend fun getExchangeRate(base: String): ExchangeRate
}

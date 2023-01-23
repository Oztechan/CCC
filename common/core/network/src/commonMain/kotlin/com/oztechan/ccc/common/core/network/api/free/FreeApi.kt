package com.oztechan.ccc.common.core.network.api.free

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface FreeApi {
    suspend fun getConversion(base: String): ExchangeRate
}

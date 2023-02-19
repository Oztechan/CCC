package com.oztechan.ccc.backend.controller.api

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface APIController {
    suspend fun getExchangeRateByBase(base: String): ExchangeRate?
}

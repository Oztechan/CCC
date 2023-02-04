package com.oztechan.ccc.backend.controller.server

import com.oztechan.ccc.common.core.network.model.ExchangeRate

internal interface ServerController {
    suspend fun getExchangeRateByBase(base: String): ExchangeRate?
}

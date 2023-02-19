package com.oztechan.ccc.common.core.network.api.premium

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface PremiumApi {
    suspend fun getExchangeRate(base: String): ExchangeRate
}

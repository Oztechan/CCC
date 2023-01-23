package com.oztechan.ccc.common.core.network.api.premium

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface PremiumApi {
    suspend fun getConversion(base: String): ExchangeRate
}

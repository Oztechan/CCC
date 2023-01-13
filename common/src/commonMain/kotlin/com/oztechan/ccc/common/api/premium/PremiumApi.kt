package com.oztechan.ccc.common.api.premium

import com.oztechan.ccc.common.api.model.ExchangeRate

internal interface PremiumApi {
    suspend fun getConversion(base: String): ExchangeRate
}

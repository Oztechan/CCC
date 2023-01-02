package com.oztechan.ccc.common.api.premium

import com.oztechan.ccc.common.api.model.CurrencyResponse

internal interface PremiumApi {
    suspend fun getConversion(base: String): CurrencyResponse
}

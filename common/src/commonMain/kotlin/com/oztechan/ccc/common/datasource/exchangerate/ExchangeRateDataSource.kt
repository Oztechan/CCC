package com.oztechan.ccc.common.datasource.exchangerate

import com.oztechan.ccc.common.core.network.model.ExchangeRate

interface ExchangeRateDataSource {
    suspend fun getExchangeRateByBase(baseName: String): ExchangeRate?
}

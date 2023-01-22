/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller.server

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import com.oztechan.ccc.common.datasource.exchangerate.ExchangeRateDataSource

internal class ServerControllerImpl(
    private val exchangeRateDataSource: ExchangeRateDataSource
) : ServerController {
    override suspend fun getExchangeRateByBase(base: String): ExchangeRate? {
        Logger.i { "ServerControllerImpl getExchangeRateByBase" }
        return exchangeRateDataSource.getExchangeRateByBase(base)
    }
}

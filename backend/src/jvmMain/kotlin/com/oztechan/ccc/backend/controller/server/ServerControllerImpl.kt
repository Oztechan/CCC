/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller.server

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import com.oztechan.ccc.common.data.datasource.conversion.ConversionDataSource

internal class ServerControllerImpl(
    private val conversionDataSource: ConversionDataSource
) : ServerController {
    override suspend fun getExchangeRateByBase(base: String): ExchangeRate? {
        Logger.i { "ServerControllerImpl getExchangeRateByBase" }
        return conversionDataSource
            .getConversionByBase(base.uppercase())
            ?.toExchangeRateAPIModel()
    }
}

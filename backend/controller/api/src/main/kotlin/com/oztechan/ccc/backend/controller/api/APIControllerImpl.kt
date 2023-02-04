/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller.api

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.api.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource

internal class APIControllerImpl(
    private val conversionDataSource: ConversionDataSource
) : APIController {
    override suspend fun getExchangeRateByBase(base: String): ExchangeRate? {
        Logger.i { "ServerControllerImpl getExchangeRateByBase" }
        return conversionDataSource
            .getConversionByBase(base.uppercase())
            ?.toExchangeRateAPIModel()
    }
}

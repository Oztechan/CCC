/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller.server

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource

internal class ServerControllerImpl(
    private val conversionDataSource: ConversionDataSource
) : ServerController {
    override suspend fun getCurrencyResponseTextByBase(base: String): String? {
        Logger.i { "ServerControllerImpl getCurrencyResponseTextByBase" }
        return conversionDataSource.getCurrencyResponseTextByBase(base)
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller.server

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource

internal class ServerControllerImpl(
    private val offlineRatesDataSource: OfflineRatesDataSource
) : ServerController {
    override suspend fun getOfflineCurrencyResponseByBase(base: String): String? {
        Logger.i { "ServerControllerImpl getOfflineCurrencyResponseByBase" }
        return offlineRatesDataSource.getOfflineCurrencyResponseByBase(base)
    }
}

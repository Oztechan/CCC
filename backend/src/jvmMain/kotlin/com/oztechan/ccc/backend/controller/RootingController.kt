/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource

class RootingController(
    private val offlineRatesDataSource: OfflineRatesDataSource
) {
    suspend fun getOfflineCurrencyResponseByBase(
        base: String
    ) = offlineRatesDataSource.getOfflineCurrencyResponseByBase(base)
}

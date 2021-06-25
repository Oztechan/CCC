/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository

class RootingController(
    private val offlineRatesRepository: OfflineRatesRepository
) {
    fun getOfflineCurrencyResponseByBase(
        base: String
    ) = offlineRatesRepository.getOfflineCurrencyResponseByBase(base)
}

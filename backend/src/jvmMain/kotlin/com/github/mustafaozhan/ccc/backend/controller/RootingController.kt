/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import com.github.mustafaozhan.ccc.common.db.dao.OfflineRatesDao

class RootingController(
    private val offlineRatesDao: OfflineRatesDao
) {
    fun getOfflineCurrencyResponseByBase(
        base: String
    ) = offlineRatesDao.getOfflineCurrencyResponseByBase(base)
}

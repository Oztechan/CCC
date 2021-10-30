/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val NUMBER_OF_REFRESH_IN_DAY = 3

class ApiController(
    private val apiRepository: ApiRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) {
    fun startSyncApi() = CoroutineScope(Dispatchers.IO).launch {
        Logger.i { "Api refreshApi" }
        while (isActive) {
            Logger.i { "refreshing" }
            updateCurrencies()

            delay(DAY / NUMBER_OF_REFRESH_IN_DAY)
        }
    }

    private suspend fun updateCurrencies() {
        CurrencyType.values().forEach { base ->

            delay(SECOND)

            apiRepository
                .getRatesViaApi(base.name)
                .execute({ currencyResponse ->
                    offlineRatesRepository.insertOfflineRates(currencyResponse)
                }, { error ->
                    Logger.e(error) { error.message.toString() }
                })
        }
    }
}

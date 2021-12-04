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

private const val NUMBER_OF_REFRESH_IN_DAY_POPULAR = 24
private const val NUMBER_OF_REFRESH_IN_DAY_UN_POPULAR = 4

class ApiController(
    private val apiRepository: ApiRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) {
    fun startSyncApi() {
        Logger.i { "ApiController startSyncApi" }

        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                updatePopularCurrencies()
                delay(DAY / NUMBER_OF_REFRESH_IN_DAY_POPULAR)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                updateUnPopularCurrencies()
                delay(DAY / NUMBER_OF_REFRESH_IN_DAY_UN_POPULAR)
            }
        }
    }

    private suspend fun updatePopularCurrencies() {
        Logger.i { "ApiController updatePopularCurrencies" }

        CurrencyType.getPopularCurrencies().forEach { base ->

            delay(SECOND)

            apiRepository.getRatesByPremiumAPI(base.name)
                .execute(
                    success = { offlineRatesRepository.insertOfflineRates(it) },
                    error = { Logger.e(it) { it.message.toString() } }
                )
        }
    }

    private suspend fun updateUnPopularCurrencies() {
        Logger.i { "ApiController updateUnPopularCurrencies" }

        CurrencyType.getNonPopularCurrencies().forEach { base ->

            delay(SECOND)

            apiRepository.getRatesByAPI(base.name)
                .execute(
                    success = { offlineRatesRepository.insertOfflineRates(it) },
                    error = { Logger.e(it) { it.message.toString() } }
                )
        }
    }
}

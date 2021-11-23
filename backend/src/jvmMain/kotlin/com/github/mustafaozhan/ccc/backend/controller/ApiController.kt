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

        updatePopularCurrencies()
        updateUnPopularCurrencies()
    }

    private fun updatePopularCurrencies() = CoroutineScope(Dispatchers.IO).launch {
        Logger.i { "ApiController updatePopularCurrencies" }

        while (isActive) {
            CurrencyType.getPopularCurrencies().forEach { base ->

                delay(SECOND)

                apiRepository
                    .getPopularRates(base.name)
                    .execute({ currencyResponse ->
                        offlineRatesRepository.insertOfflineRates(currencyResponse)
                    }, { error ->
                        Logger.e(error) { error.message.toString() }
                    })
            }

            delay(DAY / NUMBER_OF_REFRESH_IN_DAY_POPULAR)
        }
    }

    private fun updateUnPopularCurrencies() = CoroutineScope(Dispatchers.IO).launch {
        Logger.i { "ApiController updateUnPopularCurrencies" }

        while (isActive) {
            CurrencyType.values()
                .filter { !CurrencyType.getPopularCurrencies().contains(it) }
                .forEach { base ->

                    delay(SECOND)

                    apiRepository
                        .getUnPopularRates(base.name)
                        .execute({ currencyResponse ->
                            offlineRatesRepository.insertOfflineRates(currencyResponse)
                        }, { error ->
                            Logger.e(error) { error.message.toString() }
                        })
                }

            delay(DAY / NUMBER_OF_REFRESH_IN_DAY_UN_POPULAR)
        }
    }
}

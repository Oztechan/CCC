/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.repository.api

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.backend.util.fillMissingRatesWith
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.model.CurrencyType
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.SECOND
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ApiRepositoryImpl(
    private val premiumApiService: PremiumApiService,
    private val freeApiService: FreeApiService,
    private val offlineRatesDataSource: OfflineRatesDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : ApiRepository {

    override fun startSyncApi() {
        Logger.i { "ApiRepositoryImpl startSyncApi" }

        CoroutineScope(ioDispatcher).launch {
            while (isActive) {
                updatePopularCurrencies()
                delay(DAY / NUMBER_OF_REFRESH_IN_A_DAY_POPULAR)
            }
        }

        CoroutineScope(ioDispatcher).launch {
            while (isActive) {
                updateUnPopularCurrencies()
                delay(DAY / NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR)
            }
        }
    }

    override suspend fun getOfflineCurrencyResponseByBase(base: String): String? {
        Logger.i { "ApiRepositoryImpl getOfflineCurrencyResponseByBase" }
        return offlineRatesDataSource.getOfflineCurrencyResponseByBase(base)
    }

    private suspend fun updatePopularCurrencies() {
        Logger.i { "ApiController updatePopularCurrencies" }

        CurrencyType.getPopularCurrencies().forEach { base ->

            delay(SECOND)

            // non premium call for filling null values
            runCatching { freeApiService.getRates(base.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { nonPremiumResponse ->

                    // premium api call
                    runCatching { premiumApiService.getRates(base.name) }
                        .onFailure { Logger.e(it) }
                        .onSuccess { premiumResponse ->
                            offlineRatesDataSource.insertOfflineRates(
                                premiumResponse.fillMissingRatesWith(nonPremiumResponse)
                            )
                        }
                }
        }
    }

    private suspend fun updateUnPopularCurrencies() {
        Logger.i { "ApiController updateUnPopularCurrencies" }

        CurrencyType.getNonPopularCurrencies().forEach { base ->

            delay(SECOND)

            runCatching { freeApiService.getRates(base.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { offlineRatesDataSource.insertOfflineRates(it) }
        }
    }

    companion object {
        private const val NUMBER_OF_REFRESH_IN_A_DAY_POPULAR = 24
        private const val NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR = 3
    }
}

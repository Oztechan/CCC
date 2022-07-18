/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.controller

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.model.CurrencyResponse
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

private const val NUMBER_OF_REFRESH_IN_A_DAY_POPULAR = 24
private const val NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR = 3

class ApiController(
    private val premiumApiService: PremiumApiService,
    private val freeApiService: FreeApiService,
    private val offlineRatesRepository: OfflineRatesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun startSyncApi() {
        Logger.i { "ApiController startSyncApi" }

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
                            offlineRatesRepository.insertOfflineRates(
                                getModifiedResponse(nonPremiumResponse, premiumResponse)
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
                .onSuccess { offlineRatesRepository.insertOfflineRates(it) }
        }
    }

    private fun getModifiedResponse(
        nonPremiumResponse: CurrencyResponse,
        premiumResponse: CurrencyResponse
    ): CurrencyResponse {

        premiumResponse.rates = premiumResponse.rates.copy(
            btc = nonPremiumResponse.rates.btc,
            clf = nonPremiumResponse.rates.clf,
            cnh = nonPremiumResponse.rates.cnh,
            jep = nonPremiumResponse.rates.jep,
            kpw = nonPremiumResponse.rates.kpw,
            mro = nonPremiumResponse.rates.mro,
            std = nonPremiumResponse.rates.std,
            svc = nonPremiumResponse.rates.svc,
            xag = nonPremiumResponse.rates.xag,
            xau = nonPremiumResponse.rates.xau,
            xpd = nonPremiumResponse.rates.xpd,
            xpt = nonPremiumResponse.rates.xpt,
            zwl = nonPremiumResponse.rates.zwl
        )

        return premiumResponse
    }
}

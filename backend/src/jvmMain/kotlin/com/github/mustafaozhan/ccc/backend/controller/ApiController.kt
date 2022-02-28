/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
import com.github.mustafaozhan.logmob.e
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val NUMBER_OF_REFRESH_IN_A_DAY_POPULAR = 24
private const val NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR = 3

class ApiController(
    private val apiRepository: ApiRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) {
    fun startSyncApi() {
        Logger.i { "ApiController startSyncApi" }

        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                updatePopularCurrencies()
                delay(DAY / NUMBER_OF_REFRESH_IN_A_DAY_POPULAR)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
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
            runCatching { apiRepository.getRatesByAPI(base.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { nonPremiumResponse ->

                    // premium api call
                    runCatching { apiRepository.getRatesByPremiumAPI(base.name) }
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

            runCatching { apiRepository.getRatesByAPI(base.name) }
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

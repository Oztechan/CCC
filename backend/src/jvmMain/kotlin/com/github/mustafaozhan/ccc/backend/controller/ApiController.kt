/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
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
            apiRepository.getRatesByAPI(base.name)
                .execute(
                    success = { nonPremiumResponse ->

                        // premium api call
                        CoroutineScope(Dispatchers.IO).launch {
                            apiRepository.getRatesByPremiumAPI(base.name)
                                .execute(
                                    success = { premiumResponse ->
                                        offlineRatesRepository.insertOfflineRates(
                                            getModifiedResponse(nonPremiumResponse, premiumResponse)
                                        )
                                    },
                                    error = { Logger.e(it) { it.message.toString() } }
                                )
                        }

                    },
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

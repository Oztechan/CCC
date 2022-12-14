package com.oztechan.ccc.backend.controller.client

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.backend.util.fillMissingRatesWith
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.model.CurrencyType
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.util.SECOND
import kotlinx.coroutines.delay

internal class ClientControllerImpl(
    private val premiumApiService: PremiumApiService,
    private val freeApiService: FreeApiService,
    private val offlineRatesDataSource: OfflineRatesDataSource
) : ClientController {

    override suspend fun syncPopularCurrencies() {
        Logger.i { "ClientControllerImpl syncPopularCurrencies" }

        CurrencyType.getPopularCurrencies().forEach { currencyType ->

            delay(SECOND)

            // non premium call for filling null values
            runCatching { freeApiService.getRates(currencyType.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { nonPremiumResponse ->

                    // premium api call
                    runCatching { premiumApiService.getRates(currencyType.name) }
                        .onFailure { Logger.e(it) }
                        .onSuccess { premiumResponse ->
                            offlineRatesDataSource.insertOfflineRates(
                                premiumResponse.fillMissingRatesWith(nonPremiumResponse)
                            )
                        }
                }
        }
    }

    override suspend fun syncUnPopularCurrencies() {
        Logger.i { "ClientControllerImpl syncUnPopularCurrencies" }

        CurrencyType.getNonPopularCurrencies().forEach { currencyType ->

            delay(SECOND)

            runCatching { freeApiService.getRates(currencyType.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { offlineRatesDataSource.insertOfflineRates(it) }
        }
    }
}

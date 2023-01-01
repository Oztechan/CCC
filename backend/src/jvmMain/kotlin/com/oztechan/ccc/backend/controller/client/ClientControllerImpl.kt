package com.oztechan.ccc.backend.controller.client

import co.touchlab.kermit.Logger
import com.github.submob.logmob.e
import com.oztechan.ccc.backend.util.fillMissingConversionWith
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.model.CurrencyType
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.util.SECOND
import kotlinx.coroutines.delay

internal class ClientControllerImpl(
    private val premiumApiService: PremiumApiService,
    private val freeApiService: FreeApiService,
    private val conversionDataSource: ConversionDataSource
) : ClientController {

    override suspend fun syncPopularCurrencies() {
        Logger.i { "ClientControllerImpl syncPopularCurrencies" }

        CurrencyType.getPopularCurrencies().forEach { currencyType ->

            delay(SECOND)

            // non premium call for filling null values
            runCatching { freeApiService.getConversion(currencyType.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { nonPremiumResponse ->

                    // premium api call
                    runCatching { premiumApiService.getConversion(currencyType.name) }
                        .onFailure { Logger.e(it) }
                        .onSuccess { premiumResponse ->
                            conversionDataSource.insertConversion(
                                premiumResponse.fillMissingConversionWith(nonPremiumResponse)
                            )
                        }
                }
        }
    }

    override suspend fun syncUnPopularCurrencies() {
        Logger.i { "ClientControllerImpl syncUnPopularCurrencies" }

        CurrencyType.getNonPopularCurrencies().forEach { currencyType ->

            delay(SECOND)

            runCatching { freeApiService.getConversion(currencyType.name) }
                .onFailure { Logger.e(it) }
                .onSuccess { conversionDataSource.insertConversion(it) }
        }
    }
}

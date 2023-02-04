package com.oztechan.ccc.backend.controller.sync

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.service.free.FreeApiService
import com.oztechan.ccc.backend.service.premium.PremiumApiService
import com.oztechan.ccc.common.core.model.CurrencyType
import com.oztechan.ccc.common.core.model.ExchangeRate
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

internal class SyncControllerImpl(
    private val premiumApiService: PremiumApiService,
    private val freeApiService: FreeApiService,
    private val conversionDataSource: ConversionDataSource
) : SyncController {

    override suspend fun syncPopularCurrencies() {
        Logger.i { "SyncControllerImpl syncPopularCurrencies" }

        CurrencyType.getPopularCurrencies().forEach { currencyType ->

            delay(1.seconds.inWholeMilliseconds)

            // non premium call for filling null values
            runCatching { freeApiService.getConversion(currencyType.name) }
                .onFailure { Logger.e(it) { it.message.toString() } }
                .onSuccess { nonPremiumResponse ->

                    // premium api call
                    runCatching { premiumApiService.getConversion(currencyType.name) }
                        .onFailure { Logger.e(it) { it.message.toString() } }
                        .onSuccess { premiumResponse ->
                            conversionDataSource.insertConversion(
                                premiumResponse.fillMissingConversionWith(nonPremiumResponse)
                            )
                        }
                }
        }
    }

    override suspend fun syncUnPopularCurrencies() {
        Logger.i { "SyncControllerImpl syncUnPopularCurrencies" }

        CurrencyType.getNonPopularCurrencies().forEach { currencyType ->

            delay(1.seconds.inWholeMilliseconds)

            runCatching { freeApiService.getConversion(currencyType.name) }
                .onFailure { Logger.e(it) { it.message.toString() } }
                .onSuccess { conversionDataSource.insertConversion(it) }
        }
    }

    private fun ExchangeRate.fillMissingConversionWith(
        nonPremiumResponse: ExchangeRate
    ) = apply {
        conversion = conversion.copy(
            btc = nonPremiumResponse.conversion.btc,
            clf = nonPremiumResponse.conversion.clf,
            cnh = nonPremiumResponse.conversion.cnh,
            jep = nonPremiumResponse.conversion.jep,
            kpw = nonPremiumResponse.conversion.kpw,
            mro = nonPremiumResponse.conversion.mro,
            std = nonPremiumResponse.conversion.std,
            svc = nonPremiumResponse.conversion.svc,
            xag = nonPremiumResponse.conversion.xag,
            xau = nonPremiumResponse.conversion.xau,
            xpd = nonPremiumResponse.conversion.xpd,
            xpt = nonPremiumResponse.conversion.xpt,
            zwl = nonPremiumResponse.conversion.zwl
        )
    }
}

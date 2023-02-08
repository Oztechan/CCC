package com.oztechan.ccc.backend.controller.sync

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.service.free.FreeApiService
import com.oztechan.ccc.backend.service.premium.PremiumApiService
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.CurrencyType
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
                .onSuccess { freeConversion ->

                    // premium api call
                    runCatching { premiumApiService.getConversion(currencyType.name) }
                        .onFailure { Logger.e(it) { it.message.toString() } }
                        .onSuccess { premiumConversion ->
                            conversionDataSource.insertConversion(
                                premiumConversion.fillMissingConversionWith(
                                    freeConversion
                                )
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

    private fun Conversion.fillMissingConversionWith(nonPremiumConversion: Conversion) = apply {
        btc = nonPremiumConversion.btc
        clf = nonPremiumConversion.clf
        cnh = nonPremiumConversion.cnh
        jep = nonPremiumConversion.jep
        kpw = nonPremiumConversion.kpw
        mro = nonPremiumConversion.mro
        std = nonPremiumConversion.std
        svc = nonPremiumConversion.svc
        xag = nonPremiumConversion.xag
        xau = nonPremiumConversion.xau
        xpd = nonPremiumConversion.xpd
        xpt = nonPremiumConversion.xpt
        zwl = nonPremiumConversion.zwl
    }
}

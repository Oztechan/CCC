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
                                premiumConversion.fillMissingRatesWith(freeConversion)
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

    // unsupported currencies by premium API
    private fun Conversion.fillMissingRatesWith(freeConversion: Conversion) = apply {
        btc = freeConversion.btc
        clf = freeConversion.clf
        cnh = freeConversion.cnh
        kpw = freeConversion.kpw
        mro = freeConversion.mro
        std = freeConversion.std
        svc = freeConversion.svc
        xag = freeConversion.xag
        xau = freeConversion.xau
        xpd = freeConversion.xpd
        xpt = freeConversion.xpt
    }
}

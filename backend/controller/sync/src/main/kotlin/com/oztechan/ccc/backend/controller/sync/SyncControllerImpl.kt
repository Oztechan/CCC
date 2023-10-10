package com.oztechan.ccc.backend.controller.sync

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.service.premium.PremiumApiService
import com.oztechan.ccc.common.core.model.CurrencyType
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

internal class SyncControllerImpl(
    private val premiumApiService: PremiumApiService,
    private val conversionDataSource: ConversionDataSource
) : SyncController {

    override suspend fun syncPrimaryCurrencies() {
        Logger.v { "SyncControllerImpl syncPrimaryCurrencies" }
        CurrencyType.getPrimaryCurrencies().syncCurrencies()
    }

    override suspend fun syncSecondaryCurrencies() {
        Logger.v { "SyncControllerImpl syncSecondaryCurrencies" }
        CurrencyType.getSecondaryCurrencies().syncCurrencies()
    }

    override suspend fun syncTertiaryCurrencies() {
        Logger.v { "SyncControllerImpl syncTertiaryCurrencies" }
        CurrencyType.getTertiaryCurrencies().syncCurrencies()
    }

    override suspend fun syncUnPopularCurrencies() {
        Logger.v { "SyncControllerImpl syncUnPopularCurrencies" }
        CurrencyType.getNonPopularCurrencies().syncCurrencies()
    }

    private suspend fun List<CurrencyType>.syncCurrencies() = forEach { currencyType ->

        delay(1.seconds.inWholeMilliseconds)

        runCatching { premiumApiService.getConversion(currencyType.name) }
            .onFailure { Logger.w(it) { it.message.toString() } }
            .onSuccess { conversionDataSource.insertConversion(it) }
    }
}

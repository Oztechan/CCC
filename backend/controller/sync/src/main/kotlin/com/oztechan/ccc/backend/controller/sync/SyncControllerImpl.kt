package com.oztechan.ccc.backend.controller.sync

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.service.premium.PremiumApiService
import com.oztechan.ccc.common.core.model.CurrencyType
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import kotlinx.coroutines.delay
import kotlin.time.Duration

internal class SyncControllerImpl(
    private val premiumApiService: PremiumApiService,
    private val conversionDataSource: ConversionDataSource
) : SyncController {

    override suspend fun syncPrimaryCurrencies(delayDuration: Duration) {
        Logger.v { "SyncControllerImpl syncPrimaryCurrencies" }
        CurrencyType.getPrimaryCurrencies().syncCurrencies(delayDuration)
    }

    override suspend fun syncSecondaryCurrencies(delayDuration: Duration) {
        Logger.v { "SyncControllerImpl syncSecondaryCurrencies" }
        CurrencyType.getSecondaryCurrencies().syncCurrencies(delayDuration)
    }

    override suspend fun syncTertiaryCurrencies(delayDuration: Duration) {
        Logger.v { "SyncControllerImpl syncTertiaryCurrencies" }
        CurrencyType.getTertiaryCurrencies().syncCurrencies(delayDuration)
    }

    override suspend fun syncUnPopularCurrencies(delayDuration: Duration) {
        Logger.v { "SyncControllerImpl syncUnPopularCurrencies" }
        CurrencyType.getNonPopularCurrencies().syncCurrencies(delayDuration)
    }

    private suspend fun List<CurrencyType>.syncCurrencies(
        delayDuration: Duration
    ) = forEach { currencyType ->
        runCatching { premiumApiService.getConversion(currencyType.name) }
            .onFailure { Logger.w(it) { it.message.toString() } }
            .onSuccess { conversionDataSource.insertConversion(it) }

        delay(delayDuration.inWholeMilliseconds)
    }
}

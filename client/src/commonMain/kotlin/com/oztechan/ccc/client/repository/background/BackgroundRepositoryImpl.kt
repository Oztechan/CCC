package com.oztechan.ccc.client.repository.background

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.util.getConversionByCode
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService
import kotlinx.coroutines.runBlocking

internal class BackgroundRepositoryImpl(
    private val watchersDataSource: WatcherDataSource,
    private val backendApiService: BackendApiService,
) : BackgroundRepository {

    init {
        Logger.d { "BackgroundRepositoryImpl init" }
    }

    @Suppress("LabeledExpression", "TooGenericExceptionCaught")
    override fun shouldSendNotification() = try {
        Logger.d { "BackgroundRepositoryImpl shouldSendNotification" }

        runBlocking {
            watchersDataSource.getWatchers().forEach { watcher ->
                backendApiService
                    .getConversion(watcher.base)
                    .conversion
                    .getConversionByCode(watcher.target)
                    ?.let { conversionRate ->
                        when {
                            watcher.isGreater && conversionRate > watcher.rate -> return@runBlocking true
                            !watcher.isGreater && conversionRate < watcher.rate -> return@runBlocking true
                        }
                    }
            }
            return@runBlocking false
        }
    } catch (e: Exception) {
        Logger.w { "BackgroundRepositoryImpl shouldSendNotification error: $e" }
        false
    }
}

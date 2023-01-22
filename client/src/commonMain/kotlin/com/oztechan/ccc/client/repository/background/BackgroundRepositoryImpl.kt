package com.oztechan.ccc.client.repository.background

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.util.getRateFromCode
import com.oztechan.ccc.common.data.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.data.service.backend.BackendApiService
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
                    .getRateFromCode(watcher.target)
                    ?.let { rate ->
                        when {
                            watcher.isGreater && rate > watcher.rate -> return@runBlocking true
                            !watcher.isGreater && rate < watcher.rate -> return@runBlocking true
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

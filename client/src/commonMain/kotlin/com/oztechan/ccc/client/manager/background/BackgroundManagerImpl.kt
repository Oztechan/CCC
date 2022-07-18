package com.oztechan.ccc.client.manager.background

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.util.getConversionByName
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import com.oztechan.ccc.common.service.backend.BackendApiService
import kotlinx.coroutines.runBlocking

class BackgroundManagerImpl(
    private val watchersRepository: WatcherRepository,
    private val backendApiService: BackendApiService,
) : BackgroundManager {

    init {
        Logger.d { "BackgroundManagerImpl init" }
    }

    @Suppress("LabeledExpression", "TooGenericExceptionCaught")
    override fun shouldSendNotification() = try {
        Logger.d { "BackgroundManagerImpl shouldSendNotification" }

        runBlocking {
            watchersRepository.getWatchers().forEach { watcher ->
                backendApiService
                    .getRates(watcher.base)
                    .rates
                    .getConversionByName(watcher.target)
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
        Logger.w { "BackgroundManagerImpl shouldSendNotification error: $e" }
        false
    }
}

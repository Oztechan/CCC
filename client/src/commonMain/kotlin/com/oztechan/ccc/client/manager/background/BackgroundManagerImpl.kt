package com.oztechan.ccc.client.manager.background

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.util.getConversionByName
import com.oztechan.ccc.common.api.repo.ApiRepository
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import kotlinx.coroutines.runBlocking

class BackgroundManagerImpl(
    private val watchersRepository: WatcherRepository,
    private val apiRepository: ApiRepository
) : BackgroundManager {

    init {
        Logger.d { "BackgroundManagerImpl init" }
    }


    @Suppress("LabeledExpression")
    override fun shouldSendNotification() = runBlocking {
        Logger.d { "BackgroundManagerImpl checkNotifications" }

        watchersRepository.getWatchers().forEach { watcher ->
            apiRepository
                .getRatesByBackend(watcher.base)
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
}

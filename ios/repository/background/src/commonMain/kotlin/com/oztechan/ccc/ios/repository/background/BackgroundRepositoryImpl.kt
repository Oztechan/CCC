package com.oztechan.ccc.ios.repository.background

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import kotlinx.coroutines.runBlocking

internal class BackgroundRepositoryImpl(
    private val watchersDataSource: WatcherDataSource,
    private val backendApiService: BackendApiService,
) : BackgroundRepository {

    init {
        Logger.v { "BackgroundRepositoryImpl init" }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun shouldSendNotification() = try {
        Logger.v { "BackgroundRepositoryImpl shouldSendNotification" }

        runBlocking {
            watchersDataSource.getWatchers().forEach { watcher ->

                runCatching { backendApiService.getConversion(watcher.source) }
                    .onSuccess {
                        it.getRateFromCode(watcher.target)?.let { rate ->
                            when {
                                watcher.isGreater && rate > watcher.rate -> return@runBlocking true
                                !watcher.isGreater && rate < watcher.rate -> return@runBlocking true
                            }
                        }
                    }.onFailure {
                        Logger.e(it) { "BackgroundRepositoryImpl shouldSendNotification error onFailure: $it" }
                    }
            }
            return@runBlocking false
        }
    } catch (e: Exception) {
        Logger.e(e) { "BackgroundRepositoryImpl shouldSendNotification error catch: $e" }
        false
    }
}

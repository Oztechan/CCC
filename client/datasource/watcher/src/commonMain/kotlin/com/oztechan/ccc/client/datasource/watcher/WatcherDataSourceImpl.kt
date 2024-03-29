package com.oztechan.ccc.client.datasource.watcher

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.watcher.mapper.toWatcherModel
import com.oztechan.ccc.common.core.database.base.BaseDBDataSource
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.WatcherQueries
import com.oztechan.ccc.common.core.model.Watcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class WatcherDataSourceImpl(
    private val watcherQueries: WatcherQueries,
    ioDispatcher: CoroutineDispatcher
) : WatcherDataSource, BaseDBDataSource(ioDispatcher) {

    override fun getWatchersFlow(): Flow<List<Watcher>> {
        Logger.v { "WatcherDataSourceImpl getWatchersFlow" }
        return watcherQueries.getWatchers()
            .toDBFlowList()
            .map { watcherList ->
                watcherList.map { it.toWatcherModel() }
            }
    }

    override suspend fun addWatcher(base: String, target: String) = dbQuery {
        Logger.v { "WatcherDataSourceImpl addWatcher $base $target" }
        watcherQueries.addWatcher(base, target)
    }

    override suspend fun getWatchers() = dbQuery {
        Logger.v { "WatcherDataSourceImpl getWatchers" }
        watcherQueries.getWatchers()
            .executeAsList()
            .map { it.toWatcherModel() }
    }

    override suspend fun deleteWatcher(id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl deleteWatcher $id" }
        watcherQueries.deleteWatcher(id)
    }

    override suspend fun updateWatcherBaseById(base: String, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateWatcherBaseById $base $id" }
        watcherQueries.updateWatcherBaseById(base, id)
    }

    override suspend fun updateWatcherTargetById(target: String, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateWatcherTargetById $target $id" }
        watcherQueries.updateWatcherTargetById(target, id)
    }

    override suspend fun updateWatcherRelationById(isGreater: Boolean, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateWatcherRelationById $isGreater $id" }
        watcherQueries.updateWatcherRelationById(isGreater.toLong(), id)
    }

    override suspend fun updateWatcherRateById(rate: Double, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateWatcherRateById $rate $id" }
        watcherQueries.updateWatcherRateById(rate, id)
    }
}

package com.oztechan.ccc.common.datasource.watcher

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.datasource.BaseDBDataSource
import com.oztechan.ccc.common.db.sql.WatcherQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModelList
import com.oztechan.ccc.common.model.Watcher
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class WatcherDataSourceImpl(
    private val watcherQueries: WatcherQueries,
    private val ioDispatcher: CoroutineDispatcher
) : WatcherDataSource, BaseDBDataSource(ioDispatcher) {

    override fun collectWatchers(): Flow<List<Watcher>> {
        Logger.v { "WatcherDataSourceImpl collectWatchers" }
        return watcherQueries.getWatchers()
            .asFlow()
            .mapToList(ioDispatcher)
            .mapToModel()
    }

    override suspend fun addWatcher(base: String, target: String) = dbQuery {
        Logger.v { "WatcherDataSourceImpl addWatcher $base $target" }
        watcherQueries.addWatcher(base, target)
    }

    override suspend fun getWatchers() = dbQuery {
        Logger.v { "WatcherDataSourceImpl getWatchers" }
        watcherQueries.getWatchers()
            .executeAsList()
            .toModelList()
    }

    override suspend fun deleteWatcher(id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl addWatcher $id" }
        watcherQueries.deleteWatcher(id)
    }

    override suspend fun updateBaseById(base: String, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateBaseById $base $id" }
        watcherQueries.updateBaseById(base, id)
    }

    override suspend fun updateTargetById(target: String, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateTargetById $target $id" }
        watcherQueries.updateTargetById(target, id)
    }

    override suspend fun updateRelationById(isGreater: Boolean, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateRelationById $isGreater $id" }
        watcherQueries.updateRelationById(isGreater.toLong(), id)
    }

    override suspend fun updateRateById(rate: Double, id: Long) = dbQuery {
        Logger.v { "WatcherDataSourceImpl updateRateById $rate $id" }
        watcherQueries.updateRateById(rate, id)
    }
}

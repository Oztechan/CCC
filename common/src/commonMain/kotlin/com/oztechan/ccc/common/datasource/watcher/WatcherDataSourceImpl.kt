package com.oztechan.ccc.common.datasource.watcher

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.db.sql.WatcherQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModelList
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class WatcherDataSourceImpl(
    private val watcherQueries: WatcherQueries
) : WatcherDataSource {

    override fun addWatcher(
        base: String,
        target: String
    ) = watcherQueries.addWatcher(base, target)
        .also { Logger.v { "WatcherDataSourceImpl addWatcher $base $target" } }

    override fun collectWatchers() = watcherQueries
        .getWatchers()
        .asFlow()
        .mapToList()
        .mapToModel()
        .also { Logger.v { "WatcherDataSourceImpl collectWatchers" } }

    override fun getWatchers() = watcherQueries
        .getWatchers()
        .executeAsList()
        .toModelList()
        .also { Logger.v { "WatcherDataSourceImpl getWatchers" } }

    override fun deleteWatcher(id: Long) = watcherQueries
        .deleteWatcher(id)
        .also { Logger.v { "WatcherDataSourceImpl addWatcher $id" } }

    override fun updateBaseById(base: String, id: Long) = watcherQueries
        .updateBaseById(base, id)
        .also { Logger.v { "WatcherDataSourceImpl updateBaseById $base $id" } }

    override fun updateTargetById(target: String, id: Long) = watcherQueries
        .updateTargetById(target, id)
        .also { Logger.v { "WatcherDataSourceImpl updateTargetById $target $id" } }

    override fun updateRelationById(isGreater: Boolean, id: Long) = watcherQueries
        .updateRelationById(isGreater.toLong(), id)
        .also { Logger.v { "WatcherDataSourceImpl updateRelationById $isGreater $id" } }

    override fun updateRateById(rate: Double, id: Long) = watcherQueries
        .updateRateById(rate, id)
        .also { Logger.v { "WatcherDataSourceImpl updateRateById $rate $id" } }
}

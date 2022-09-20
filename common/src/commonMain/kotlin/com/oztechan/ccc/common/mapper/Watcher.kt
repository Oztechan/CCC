package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Watcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.oztechan.ccc.common.db.sql.Watcher as WatcherEntity

internal fun WatcherEntity.toModel() = Watcher(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

internal fun List<WatcherEntity>.toModelList(): List<Watcher> {
    return map { it.toModel() }
}

internal fun Flow<List<WatcherEntity>>.mapToModel(): Flow<List<Watcher>> {
    return this.map { it.toModelList() }
}

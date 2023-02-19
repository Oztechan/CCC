package com.oztechan.ccc.client.datasource.watcher.mapper

import com.oztechan.ccc.common.core.database.mapper.toBoolean
import com.oztechan.ccc.common.core.database.sql.Watcher as WatcherDBModel
import com.oztechan.ccc.common.core.model.Watcher as WatcherModel

internal fun WatcherDBModel.toWatcherModel() = WatcherModel(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

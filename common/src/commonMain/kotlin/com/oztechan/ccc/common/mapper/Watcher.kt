package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.database.sql.Watcher as WatcherDBModel
import com.oztechan.ccc.common.model.Watcher as WatcherModel

internal fun WatcherDBModel.toWatcherModel() = WatcherModel(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

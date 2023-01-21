package com.oztechan.ccc.common.core.database.mapper

import com.oztechan.ccc.common.core.database.sql.Watcher as WatcherDBModel
import com.oztechan.ccc.common.core.model.Watcher as WatcherModel

fun WatcherDBModel.toWatcherModel() = WatcherModel(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

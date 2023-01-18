package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.database.sql.Watcher as WatcherDBModel

internal fun WatcherDBModel.toModel() = Watcher(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

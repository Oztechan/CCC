package com.oztechan.ccc.client.datasource.watcher.fakes

import com.oztechan.ccc.common.core.database.sql.Watcher

object Fakes {
    private const val base = "EUR"
    private const val target = "USD"
    val watcherDBModel = Watcher(
        id = 1L,
        base = base,
        target = target,
        isGreater = 1L,
        rate = 1.1
    )
}

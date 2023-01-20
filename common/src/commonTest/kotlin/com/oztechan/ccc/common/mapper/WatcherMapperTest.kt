package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.core.database.sql.Watcher as WatcherDBModel

internal class WatcherMapperTest : BaseTest() {
    @Test
    fun toWatcherModel() {
        val dbModel = WatcherDBModel(
            id = 1L,
            base = "EUR",
            target = "USD",
            isGreater = 1L,
            rate = 1.1
        )

        val model = dbModel.toWatcherModel()

        assertEquals(dbModel.id, model.id)
        assertEquals(dbModel.base, model.base)
        assertEquals(dbModel.target, model.target)
        assertEquals(dbModel.isGreater.toBoolean(), model.isGreater)
        assertEquals(dbModel.rate, model.rate)
    }
}

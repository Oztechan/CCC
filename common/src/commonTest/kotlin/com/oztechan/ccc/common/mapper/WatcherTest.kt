package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.database.sql.Watcher as WatcherDBModel

internal class WatcherTest : BaseTest() {
    @Test
    fun toModel() {
        val dbModel = WatcherDBModel(
            id = 1L,
            base = "EUR",
            target = "USD",
            isGreater = 1L,
            rate = 1.1
        )

        val model = dbModel.toModel()

        assertEquals(dbModel.id, model.id)
        assertEquals(dbModel.base, model.base)
        assertEquals(dbModel.target, model.target)
        assertEquals(dbModel.isGreater.toBoolean(), model.isGreater)
        assertEquals(dbModel.rate, model.rate)
    }
}

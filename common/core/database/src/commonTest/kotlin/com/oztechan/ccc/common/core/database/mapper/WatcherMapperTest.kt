package com.oztechan.ccc.common.core.database.mapper

import com.oztechan.ccc.common.core.database.fakes.Fakes
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WatcherMapperTest {
    @Test
    fun toWatcherModel() {
        val model = Fakes.watcherDBModel.toWatcherModel()

        assertEquals(Fakes.watcherDBModel.id, model.id)
        assertEquals(Fakes.watcherDBModel.base, model.base)
        assertEquals(Fakes.watcherDBModel.target, model.target)
        assertEquals(Fakes.watcherDBModel.isGreater.toBoolean(), model.isGreater)
        assertEquals(Fakes.watcherDBModel.rate, model.rate)
    }
}

package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.helper.BaseTest
import com.oztechan.ccc.client.util.removeScientificNotation
import com.oztechan.ccc.common.core.model.Watcher
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WatcherTest : BaseTest() {

    private val watcher = Watcher(
        id = 1L,
        base = "EUR",
        target = "USD",
        isGreater = true,
        rate = 1.1
    )

    @Test
    fun toUIModel() {
        val uiModel = watcher.toUIModel()

        assertEquals(watcher.id, uiModel.id)
        assertEquals(watcher.base, uiModel.base)
        assertEquals(watcher.target, uiModel.target)
        assertEquals(watcher.isGreater, uiModel.isGreater)
        assertEquals(watcher.rate.removeScientificNotation(), uiModel.rate)
    }

    @Test
    fun toUIModelList() {
        val anotherWatcher = Watcher(
            id = 2L,
            base = "USD",
            target = "EUR",
            isGreater = false,
            rate = 0.1
        )
        val watcherList = listOf(watcher, anotherWatcher)

        val watcherUIModelList = watcherList.toUIModelList()

        watcherList.zip(watcherUIModelList) { first, second ->
            assertEquals(first.id, second.id)
            assertEquals(first.base, second.base)
            assertEquals(first.target, second.target)
            assertEquals(first.isGreater, second.isGreater)
            assertEquals(first.rate.removeScientificNotation(), second.rate)
        }
    }
}

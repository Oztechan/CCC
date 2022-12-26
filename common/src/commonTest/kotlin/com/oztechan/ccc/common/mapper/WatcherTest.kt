package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.database.sql.Watcher as WatcherEntity

@Suppress("OPT_IN_USAGE")
internal class WatcherTest : BaseTest() {

    private val entity = WatcherEntity(
        id = 1L,
        base = "EUR",
        target = "USD",
        isGreater = 1L,
        rate = 1.1
    )

    @Test
    fun toModel() {
        val model = entity.toModel()

        assertEquals(entity.id, model.id)
        assertEquals(entity.base, model.base)
        assertEquals(entity.target, model.target)
        assertEquals(entity.isGreater.toBoolean(), model.isGreater)
        assertEquals(entity.rate, model.rate)
    }

    @Test
    fun toModelList() {
        val anotherWatcher = WatcherEntity(
            id = 2L,
            base = "USD",
            target = "EUR",
            isGreater = 0L,
            rate = 0.1
        )
        val watcherList = listOf(entity, anotherWatcher)

        val watcherUIModelList = watcherList.toModelList()

        watcherList.zip(watcherUIModelList) { first, second ->
            assertEquals(first.id, second.id)
            assertEquals(first.base, second.base)
            assertEquals(first.target, second.target)
            assertEquals(first.isGreater.toBoolean(), second.isGreater)
            assertEquals(first.rate, second.rate)
        }
    }

    @Test
    fun mapToModel() {
        val watcherList = listOf(entity)

        val watcherListFlow = flowOf(watcherList)

        runTest {
            watcherListFlow.mapToModel().firstOrNull()?.firstOrNull()?.let {
                assertEquals(entity.id, it.id)
                assertEquals(entity.base, it.base)
                assertEquals(entity.target, it.target)
                assertEquals(entity.isGreater.toBoolean(), it.isGreater)
                assertEquals(entity.rate, it.rate)
            }
        }
    }
}

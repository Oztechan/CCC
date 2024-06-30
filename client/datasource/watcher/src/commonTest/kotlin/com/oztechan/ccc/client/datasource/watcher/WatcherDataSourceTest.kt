package com.oztechan.ccc.client.datasource.watcher

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.Watcher
import com.oztechan.ccc.common.core.database.sql.WatcherQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class WatcherDataSourceTest {

    private val subject: WatcherDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        WatcherDataSourceImpl(watcherQueries, UnconfinedTestDispatcher())
    }

    private val watcherQueries = mock<WatcherQueries>(MockMode.autoUnit)
    private val sqlDriver = mock<SqlDriver>()
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)

    private val base = "EUR"
    private val target = "USD"
    private val id = 12L

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        Watcher(id, base, target, 1L, 0.0)
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        every { sqlDriver.executeQuery(-1, "", 0, null) }
            .returns(sqlCursor)

        every { sqlCursor.next() }
            .returns(false)
    }

    @Test
    fun getWatchersFlow() = runTest {
        every { watcherQueries.getWatchers() }
            .returns(query)

        subject.getWatchersFlow()

        verify { watcherQueries.getWatchers() }
    }

    @Test
    fun addWatcher() = runTest {
        subject.addWatcher(base, target)

        verify { watcherQueries.addWatcher(base, target) }
    }

    @Test
    fun getWatchers() = runTest {
        every { watcherQueries.getWatchers() }
            .returns(query)

        subject.getWatchers()

        verify { watcherQueries.getWatchers() }
    }

    @Test
    fun deleteWatcher() = runTest {
        subject.deleteWatcher(id)

        verify { watcherQueries.deleteWatcher(id) }
    }

    @Test
    fun updateWatcherBaseById() = runTest {
        subject.updateWatcherBaseById(base, id)

        verify { watcherQueries.updateWatcherBaseById(base, id) }
    }

    @Test
    fun updateWatcherTargetById() = runTest {
        subject.updateWatcherTargetById(target, id)

        verify { watcherQueries.updateWatcherTargetById(target, id) }
    }

    @Test
    fun updateWatcherRelationById() = runTest {
        val relation = Random.nextBoolean()
        subject.updateWatcherRelationById(relation, id)

        verify { watcherQueries.updateWatcherRelationById(relation.toLong(), id) }
    }

    @Test
    fun updateWatcherRateById() = runTest {
        val rate = 1.2

        subject.updateWatcherRateById(rate, id)

        verify { watcherQueries.updateWatcherRateById(rate, id) }
    }
}

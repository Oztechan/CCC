package com.oztechan.ccc.client.datasource.watcher

import com.github.submob.logmob.initTestLogger
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.Watcher
import com.oztechan.ccc.common.core.database.sql.WatcherQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class WatcherDataSourceTest {

    private val subject: WatcherDataSource by lazy {
        WatcherDataSourceImpl(watcherQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val watcherQueries = mock(classOf<WatcherQueries>())

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = mock(classOf<SqlCursor>())

    private val base = "EUR"
    private val target = "USD"
    private val id = 12L

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        Watcher(id, base, target, 1L, 0.0)
    }

    @BeforeTest
    fun setup() {
        initTestLogger()

        given(sqlDriver)
            .invocation { executeQuery(-1, "", 0, null) }
            .thenReturn(sqlCursor)

        given(sqlCursor)
            .invocation { next() }
            .thenReturn(false)
    }

    @Test
    fun getWatchersFlow() = runTest {
        given(watcherQueries)
            .invocation { getWatchers() }
            .then { query }

        subject.getWatchersFlow()

        verify(watcherQueries)
            .coroutine { getWatchers() }
            .wasInvoked()
    }

    @Test
    fun addWatcher() = runTest {
        subject.addWatcher(base, target)

        verify(watcherQueries)
            .invocation { addWatcher(base, target) }
            .wasInvoked()
    }

    @Test
    fun getWatchers() = runTest {
        given(watcherQueries)
            .invocation { getWatchers() }
            .then { query }

        subject.getWatchers()

        verify(watcherQueries)
            .coroutine { getWatchers() }
            .wasInvoked()
    }

    @Test
    fun deleteWatcher() = runTest {
        subject.deleteWatcher(id)

        verify(watcherQueries)
            .invocation { deleteWatcher(id) }
            .wasInvoked()
    }

    @Test
    fun updateWatcherBaseById() = runTest {
        subject.updateWatcherBaseById(base, id)

        verify(watcherQueries)
            .invocation { updateWatcherBaseById(base, id) }
            .wasInvoked()
    }

    @Test
    fun updateWatcherTargetById() = runTest {
        subject.updateWatcherTargetById(target, id)

        verify(watcherQueries)
            .invocation { updateWatcherTargetById(target, id) }
            .wasInvoked()
    }

    @Test
    fun updateWatcherRelationById() = runTest {
        val relation = Random.nextBoolean()
        subject.updateWatcherRelationById(relation, id)

        verify(watcherQueries)
            .invocation { updateWatcherRelationById(relation.toLong(), id) }
            .wasInvoked()
    }

    @Test
    fun updateWatcherRateById() = runTest {
        val rate = 1.2

        subject.updateWatcherRateById(rate, id)

        verify(watcherQueries)
            .invocation { updateWatcherRateById(rate, id) }
            .wasInvoked()
    }
}

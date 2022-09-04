package com.oztechan.ccc.common.datasource

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.db.sql.WatcherQueries
import com.oztechan.ccc.common.mapper.toLong
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
class WatcherDataSourceTest {
    @Mock
    private val watcherQueries = mock(classOf<WatcherQueries>())

    private val dataSource: WatcherDataSource by lazy {
        WatcherDataSourceImpl(watcherQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    private val base = "EUR"
    private val target = "USD"
    private val id = 12L

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun addWatcher() = runTest {
        dataSource.addWatcher(base, target)

        verify(watcherQueries)
            .invocation { addWatcher(base, target) }
            .wasInvoked()
    }


    @Test
    fun deleteWatcher() = runTest {
        dataSource.deleteWatcher(id)

        verify(watcherQueries)
            .invocation { deleteWatcher(id) }
            .wasInvoked()
    }

    @Test
    fun updateBaseById() = runTest {
        dataSource.updateBaseById(base, id)

        verify(watcherQueries)
            .invocation { updateBaseById(base, id) }
            .wasInvoked()
    }

    @Test
    fun updateTargetById() = runTest {
        dataSource.updateTargetById(target, id)

        verify(watcherQueries)
            .invocation { updateTargetById(target, id) }
            .wasInvoked()
    }

    @Test
    fun updateRelationById() = runTest {
        val relation = Random.nextBoolean()
        dataSource.updateRelationById(relation, id)

        verify(watcherQueries)
            .invocation { updateRelationById(relation.toLong(), id) }
            .wasInvoked()
    }

    @Test
    fun updateRateById() = runTest {
        val rate = 1.2

        dataSource.updateRateById(rate, id)

        verify(watcherQueries)
            .invocation { updateRateById(rate, id) }
            .wasInvoked()
    }
}

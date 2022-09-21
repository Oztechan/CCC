package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.db.sql.WatcherQueries
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class WatcherDataSourceTest : BaseSubjectTest<WatcherDataSource>() {

    override val subject: WatcherDataSource by lazy {
        WatcherDataSourceImpl(watcherQueries, createTestDispatcher())
    }

    @Mock
    private val watcherQueries = mock(classOf<WatcherQueries>())

    private val base = "EUR"
    private val target = "USD"
    private val id = 12L

    @Test
    fun addWatcher() = runTest {
        subject.addWatcher(base, target)

        verify(watcherQueries)
            .invocation { addWatcher(base, target) }
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
    fun updateBaseById() = runTest {
        subject.updateBaseById(base, id)

        verify(watcherQueries)
            .invocation { updateBaseById(base, id) }
            .wasInvoked()
    }

    @Test
    fun updateTargetById() = runTest {
        subject.updateTargetById(target, id)

        verify(watcherQueries)
            .invocation { updateTargetById(target, id) }
            .wasInvoked()
    }

    @Test
    fun updateRelationById() = runTest {
        val relation = Random.nextBoolean()
        subject.updateRelationById(relation, id)

        verify(watcherQueries)
            .invocation { updateRelationById(relation.toLong(), id) }
            .wasInvoked()
    }

    @Test
    fun updateRateById() = runTest {
        val rate = 1.2

        subject.updateRateById(rate, id)

        verify(watcherQueries)
            .invocation { updateRateById(rate, id) }
            .wasInvoked()
    }
}

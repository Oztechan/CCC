package com.oztechan.ccc.client.datasource.watcher

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class WatcherDataSourceTest {

    private val driver = createTestSqlDriver()

    private val subject: WatcherDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        WatcherDataSourceImpl(
            CurrencyConverterCalculatorDatabase(driver).watcherQueries,
            UnconfinedTestDispatcher()
        )
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun getWatchersFlow() = runTest {
        assertTrue { subject.getWatchersFlow().first().isEmpty() }

        subject.addWatcher(BASE, TARGET)

        assertEquals(1, subject.getWatchersFlow().first().size)
    }

    @Test
    fun addWatcher() = runTest {
        subject.addWatcher(BASE, TARGET)

        val watcher = subject.getWatchers().single()

        assertEquals(BASE, watcher.source)
        assertEquals(TARGET, watcher.target)
        assertTrue { watcher.isGreater }
        assertEquals(0.0, watcher.rate)
    }

    @Test
    fun getWatchers() = runTest {
        assertTrue { subject.getWatchers().isEmpty() }

        subject.addWatcher(BASE, TARGET)
        subject.addWatcher(TARGET, BASE)

        assertEquals(2, subject.getWatchers().size)
    }

    @Test
    fun deleteWatcher() = runTest {
        subject.addWatcher(BASE, TARGET)

        subject.deleteWatcher(subject.getWatchers().single().id)

        assertTrue { subject.getWatchers().isEmpty() }
    }

    @Test
    fun updateWatcherBaseById() = runTest {
        subject.addWatcher(BASE, TARGET)
        val id = subject.getWatchers().single().id

        subject.updateWatcherBaseById(TARGET, id)

        assertEquals(TARGET, subject.getWatchers().single().source)
    }

    @Test
    fun updateWatcherTargetById() = runTest {
        subject.addWatcher(BASE, TARGET)
        val id = subject.getWatchers().single().id

        subject.updateWatcherTargetById(BASE, id)

        assertEquals(BASE, subject.getWatchers().single().target)
    }

    @Test
    fun updateWatcherRelationById() = runTest {
        subject.addWatcher(BASE, TARGET)
        val id = subject.getWatchers().single().id

        subject.updateWatcherRelationById(false, id)

        assertEquals(false, subject.getWatchers().single().isGreater)
    }

    @Test
    fun updateWatcherRateById() = runTest {
        subject.addWatcher(BASE, TARGET)
        val id = subject.getWatchers().single().id

        subject.updateWatcherRateById(1.2, id)

        assertEquals(1.2, subject.getWatchers().single().rate)
    }

    companion object {
        private const val BASE = "EUR"
        private const val TARGET = "USD"
    }
}

package com.oztechan.ccc.client.datasource.watcher

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.WatcherQueries
import dev.mokkery.MockMode
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class WatcherDataSourceTest {

    private val sqlDriver = mock<SqlDriver>(MockMode.autoUnit)
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)
    private val statement = mock<SqlPreparedStatement>(MockMode.autoUnit)

    private val executedSql = mutableListOf<String>()

    private val subject: WatcherDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        WatcherDataSourceImpl(WatcherQueries(sqlDriver), UnconfinedTestDispatcher())
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        every { sqlDriver.currentTransaction() } returns null
        every { sqlCursor.next() } returns QueryResult.Value(false)

        every { sqlDriver.execute(any(), any(), any(), any()) } calls { args ->
            executedSql += args.arg<String>(1)
            args.arg<(SqlPreparedStatement.() -> Unit)?>(3)?.invoke(statement)
            QueryResult.Value(1L)
        }
        every { sqlDriver.executeQuery<Any?>(any(), any(), any(), any(), any()) } calls { args ->
            executedSql += args.arg<String>(1)
            args.arg<(SqlPreparedStatement.() -> Unit)?>(4)?.invoke(statement)
            args.arg<(SqlCursor) -> QueryResult<Any?>>(2).invoke(sqlCursor)
        }
    }

    @Test
    fun getWatchersFlow() = runTest {
        subject.getWatchersFlow().first()

        assertTrue { executedSql.single().contains("FROM Watcher") }
    }

    @Test
    fun addWatcher() = runTest {
        subject.addWatcher(BASE, TARGET)

        assertTrue { executedSql.single().contains("INSERT OR REPLACE INTO Watcher") }
        verify { statement.bindString(0, BASE) }
        verify { statement.bindString(1, TARGET) }
    }

    @Test
    fun getWatchers() = runTest {
        assertTrue { subject.getWatchers().isEmpty() }

        assertTrue { executedSql.single().contains("FROM Watcher") }
    }

    @Test
    fun deleteWatcher() = runTest {
        subject.deleteWatcher(ID)

        assertTrue { executedSql.single().contains("DELETE FROM Watcher WHERE id = ?") }
        verify { statement.bindLong(0, ID) }
    }

    @Test
    fun updateWatcherBaseById() = runTest {
        subject.updateWatcherBaseById(BASE, ID)

        assertTrue { executedSql.single().contains("UPDATE Watcher SET base=? WHERE id=?") }
        verify { statement.bindString(0, BASE) }
        verify { statement.bindLong(1, ID) }
    }

    @Test
    fun updateWatcherTargetById() = runTest {
        subject.updateWatcherTargetById(TARGET, ID)

        assertTrue { executedSql.single().contains("UPDATE Watcher SET target=? WHERE id=?") }
        verify { statement.bindString(0, TARGET) }
        verify { statement.bindLong(1, ID) }
    }

    @Test
    fun updateWatcherRelationById() = runTest {
        subject.updateWatcherRelationById(true, ID)

        assertTrue { executedSql.single().contains("UPDATE Watcher SET isGreater=? WHERE id=?") }
        verify { statement.bindLong(0, 1L) }
        verify { statement.bindLong(1, ID) }
    }

    @Test
    fun updateWatcherRateById() = runTest {
        subject.updateWatcherRateById(RATE, ID)

        assertTrue { executedSql.single().contains("UPDATE Watcher SET rate=? WHERE id=?") }
        verify { statement.bindDouble(0, RATE) }
        verify { statement.bindLong(1, ID) }
    }

    companion object {
        private const val BASE = "EUR"
        private const val TARGET = "USD"
        private const val ID = 12L
        private const val RATE = 1.2
    }
}

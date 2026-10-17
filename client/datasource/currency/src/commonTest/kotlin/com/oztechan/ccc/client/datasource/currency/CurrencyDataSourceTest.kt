package com.oztechan.ccc.client.datasource.currency

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.CurrencyQueries
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

internal class CurrencyDataSourceTest {

    private val sqlDriver = mock<SqlDriver>(MockMode.autoUnit)
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)
    private val statement = mock<SqlPreparedStatement>(MockMode.autoUnit)

    private val executedSql = mutableListOf<String>()

    private val subject: CurrencyDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        CurrencyDataSourceImpl(CurrencyQueries(sqlDriver), UnconfinedTestDispatcher())
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
    fun getCurrenciesFlow() = runTest {
        subject.getCurrenciesFlow().first()

        assertTrue { executedSql.single().contains("FROM Currency") }
    }

    @Test
    fun getActiveCurrenciesFlow() = runTest {
        subject.getActiveCurrenciesFlow().first()

        assertTrue { executedSql.single().contains("FROM Currency WHERE isActive=1") }
    }

    @Test
    fun getActiveCurrencies() = runTest {
        assertTrue { subject.getActiveCurrencies().isEmpty() }

        assertTrue { executedSql.single().contains("FROM Currency WHERE isActive=1") }
    }

    @Test
    fun updateCurrencyStateByCode() = runTest {
        subject.updateCurrencyStateByCode(EUR, true)

        assertTrue { executedSql.single().contains("UPDATE Currency SET isActive=? WHERE code=?") }
        verify { statement.bindLong(0, 1L) }
        verify { statement.bindString(1, EUR) }
    }

    @Test
    fun updateCurrencyStates() = runTest {
        subject.updateCurrencyStates(false)

        assertTrue { executedSql.single().contains("UPDATE Currency SET isActive=?") }
        verify { statement.bindLong(0, 0L) }
    }

    @Test
    fun getCurrencyByCode() = runTest {
        subject.getCurrencyByCode(EUR)

        assertTrue { executedSql.single().contains("FROM Currency WHERE code=?") }
        verify { statement.bindString(0, EUR) }
    }

    companion object {
        private const val EUR = "EUR"
    }
}

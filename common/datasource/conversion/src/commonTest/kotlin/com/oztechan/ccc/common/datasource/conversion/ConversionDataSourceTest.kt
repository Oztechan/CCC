package com.oztechan.ccc.common.datasource.conversion

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.conversion.fakes.Fakes
import dev.mokkery.MockMode
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ConversionDataSourceTest {

    private val sqlDriver = mock<SqlDriver>(MockMode.autoUnit)
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)
    private val statement = mock<SqlPreparedStatement>(MockMode.autoUnit)

    private val executedSql = mutableListOf<String>()

    private val subject: ConversionDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        ConversionDataSourceImpl(ConversionQueries(sqlDriver), UnconfinedTestDispatcher())
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
    fun insertConversion() = runTest {
        subject.insertConversion(Fakes.conversionModel)

        assertTrue { executedSql.single().contains("INSERT OR REPLACE INTO Conversion") }
        verify { statement.bindString(0, Fakes.conversionModel.base) }
    }

    @Test
    fun getConversionByBase() = runTest {
        assertNull(subject.getConversionByBase(Fakes.conversionModel.base))

        assertTrue { executedSql.single().contains("FROM Conversion WHERE base=?") }
        verify { statement.bindString(0, Fakes.conversionModel.base) }
    }
}

package com.oztechan.ccc.common.core.database.base

import com.oztechan.ccc.common.core.database.error.DatabaseException
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

@Suppress("OPT_IN_USAGE")
class BaseDBDataSourceTest {
    private val subject = object : BaseDBDataSource(UnconfinedTestDispatcher()) {
        suspend fun <T> query(
            suspendBlock: suspend () -> T
        ) = dbQuery {
            suspendBlock.invoke()
        }
    }

    @Test
    fun `any exception returns DatabaseException`() = runTest {
        assertFailsWith(DatabaseException::class) {
            @Suppress("TooGenericExceptionThrown")
            subject.query { throw Exception("Test exception") }
        }
    }
}

package com.oztechan.ccc.common.core.database.base

import com.oztechan.ccc.common.core.database.error.DatabaseException
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE")
class BaseDBDataSourceTest {
    private val subject = object : BaseDBDataSource(newSingleThreadContext(this::class.simpleName.toString())) {
        suspend fun <T> query(
            suspendBlock: suspend () -> T
        ) = dbQuery {
            suspendBlock.invoke()
        }
    }

    @Test
    fun `any exception returns DatabaseException`() = runTest {
        val exception = Exception("Test exception")

        assertFailsWith(DatabaseException::class) {
            subject.query { throw exception }
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception.message, it.cause.message)
        }
    }
}

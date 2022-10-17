package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.error.DatabaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal open class BaseDBDataSource(
    private val ioDispatcher: CoroutineDispatcher
) {
    @Suppress("TooGenericExceptionCaught")
    protected suspend fun <T> dbQuery(
        suspendBlock: suspend () -> T
    ): T = withContext(ioDispatcher) {
        try {
            suspendBlock.invoke()
        } catch (e: Throwable) {
            throw DatabaseException(e)
        }
    }
}

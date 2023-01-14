package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.error.DatabaseException
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
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

    protected fun <T : Any> Query<T>.toDBFlow(): Flow<Query<T>> = asFlow()
        .flowOn(ioDispatcher)
        .catch {
            throw DatabaseException(it)
        }
}

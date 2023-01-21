package com.oztechan.ccc.common.core.database.base

import com.oztechan.ccc.common.core.database.error.DatabaseException
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

open class BaseDBDataSource(private val ioDispatcher: CoroutineDispatcher) {
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

    protected fun <T : Any> Query<T>.toDBFlowList(): Flow<List<T>> = toDBFlow().mapToList(ioDispatcher)
}

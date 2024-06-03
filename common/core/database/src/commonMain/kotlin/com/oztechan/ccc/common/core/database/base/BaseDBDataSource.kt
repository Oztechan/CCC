package com.oztechan.ccc.common.core.database.base

import co.touchlab.kermit.Logger
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
            DatabaseException(e).let {
                Logger.e(it) { it.message.orEmpty() }
                throw it
            }
        }
    }

    protected fun <T : Any> Query<T>.toDBFlowList(): Flow<List<T>> = asFlow()
        .flowOn(ioDispatcher)
        .catch {
            DatabaseException(it).let { exception ->
                Logger.e(exception) { exception.message.orEmpty() }
                throw exception
            }
        }.mapToList(ioDispatcher)
}

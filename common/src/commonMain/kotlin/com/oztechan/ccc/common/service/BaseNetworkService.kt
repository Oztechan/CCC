package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.model.EmptyParameterException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class BaseNetworkService(
    private val ioDispatcher: CoroutineDispatcher
) {
    protected suspend fun <T> apiRequest(
        suspendBlock: suspend () -> T
    ): T = withContext(ioDispatcher) {
        suspendBlock.invoke()
    }

    protected fun String.withEmptyParameterCheck() = ifEmpty {
        throw EmptyParameterException()
    }
}

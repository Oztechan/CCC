package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.error.EmptyParameterException
import com.oztechan.ccc.common.error.ModelMappingException
import com.oztechan.ccc.common.error.NetworkException
import com.oztechan.ccc.common.error.TimeoutException
import com.oztechan.ccc.common.error.UnknownNetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

internal open class BaseNetworkService(
    private val ioDispatcher: CoroutineDispatcher
) {
    protected suspend fun <T> apiRequest(
        suspendBlock: suspend () -> T
    ): T = withContext(ioDispatcher) {
        makeRequest {
            suspendBlock.invoke()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> makeRequest(
        suspendBlock: suspend () -> T
    ): T = try {
        suspendBlock.invoke()
    } catch (e: Throwable) {
        throw when (e) {
            is CancellationException -> e
            is IOException -> NetworkException(e)
            is ConnectTimeoutException -> TimeoutException(e)
            is SerializationException -> ModelMappingException(e)
            else -> UnknownNetworkException(e)
        }
    }

    protected fun String.withEmptyParameterCheck() = ifEmpty {
        throw EmptyParameterException()
    }
}

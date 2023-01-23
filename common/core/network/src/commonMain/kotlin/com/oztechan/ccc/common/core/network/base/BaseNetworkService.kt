package com.oztechan.ccc.common.core.network.base

import com.oztechan.ccc.common.core.network.error.EmptyParameterException
import com.oztechan.ccc.common.core.network.error.ModelMappingException
import com.oztechan.ccc.common.core.network.error.NetworkException
import com.oztechan.ccc.common.core.network.error.TerminationException
import com.oztechan.ccc.common.core.network.error.TimeoutException
import com.oztechan.ccc.common.core.network.error.UnknownNetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

open class BaseNetworkService(private val ioDispatcher: CoroutineDispatcher) {
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
            is CancellationException -> TerminationException(e)
            is ConnectTimeoutException -> TimeoutException(e)
            is IOException -> NetworkException(e)
            is SerializationException -> ModelMappingException(e)
            else -> UnknownNetworkException(e)
        }
    }

    protected fun withEmptyParameterCheck(parameter: String) = parameter.ifEmpty {
        throw EmptyParameterException()
    }
}

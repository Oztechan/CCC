/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Result
import com.github.mustafaozhan.ccc.common.platformCoroutineContext
import com.github.mustafaozhan.temp.error.EmptyParameterException
import com.github.mustafaozhan.temp.error.ModelMappingException
import com.github.mustafaozhan.temp.error.NetworkException
import com.github.mustafaozhan.temp.error.NullBaseException
import com.github.mustafaozhan.temp.error.TimeoutException
import com.github.mustafaozhan.temp.error.UnknownNetworkException
import io.ktor.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

class ApiRepository(private val apiFactory: ApiFactory) {

    @Suppress("ThrowsCount", "TooGenericExceptionCaught")
    suspend fun <T> apiRequest(
        suspendBlock: suspend () -> T
    ) = withContext(platformCoroutineContext) {
        try {
            Result.Success(suspendBlock.invoke())
        } catch (e: Throwable) {
            Result.Error(
                when (e) {
                    is CancellationException -> e
                    is IOException -> NetworkException(e)
                    is ConnectTimeoutException -> TimeoutException(e)
                    is SerializationException -> ModelMappingException(e)
                    else -> UnknownNetworkException(e)
                }
            )
        }
    }

    suspend fun getRatesByBase(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == CurrencyType.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.getRatesByBase(base)
        }
    }
}

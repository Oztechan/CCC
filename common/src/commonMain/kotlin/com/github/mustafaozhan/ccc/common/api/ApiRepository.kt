/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.toModel
import com.github.mustafaozhan.ccc.common.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.error.ModelMappingException
import com.github.mustafaozhan.ccc.common.error.NetworkException
import com.github.mustafaozhan.ccc.common.error.NullBaseException
import com.github.mustafaozhan.ccc.common.error.TimeoutException
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Result
import com.github.mustafaozhan.ccc.common.platformCoroutineContext
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
                    else -> e
                }
            )
        }
    }

    suspend fun getRatesByBaseViaBackend(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == CurrencyType.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.getRatesByBaseViaBackend(base).toModel()
        }
    }

    suspend fun getRatesByBaseViaApi(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == CurrencyType.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.getRatesByBaseViaApi(base).toModel()
        }
    }
}

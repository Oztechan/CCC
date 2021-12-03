/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.error.ModelMappingException
import com.github.mustafaozhan.ccc.common.error.NetworkException
import com.github.mustafaozhan.ccc.common.error.TimeoutException
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.platformCoroutineContext
import com.github.mustafaozhan.ccc.common.util.Result
import io.ktor.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

internal class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {

    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> apiRequest(
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

    override suspend fun getRatesByBackend(base: String) = apiRequest {
        apiService.getRatesByBackend(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getRatesViaBackend $base" }
    }

    override suspend fun getRatesByAPI(base: String) = apiRequest {
        apiService.getRatesByAPI(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getUnPopularRates $base" }
    }

    override suspend fun getRatesByPremiumAPI(base: String) = apiRequest {
        apiService.getRatesByPremiumAPI(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getPopularRates $base" }
    }
}

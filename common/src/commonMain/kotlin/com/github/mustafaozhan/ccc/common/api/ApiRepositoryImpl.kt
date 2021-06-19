/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.api.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.api.error.ModelMappingException
import com.github.mustafaozhan.ccc.common.api.error.NetworkException
import com.github.mustafaozhan.ccc.common.api.error.TimeoutException
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.platformCoroutineContext
import com.github.mustafaozhan.ccc.common.util.Result
import com.github.mustafaozhan.logmob.kermit
import io.ktor.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

internal class ApiRepositoryImpl(
    private val apiFactory: ApiFactory
) : ApiRepository {

    @Suppress("TooGenericExceptionCaught")
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

    override suspend fun getRatesViaBackend(base: String) = apiRequest {
        if (base.isEmpty()) throw EmptyParameterException()
        else apiFactory.getRatesViaBackend(base).toModel(base)
    }.also {
        kermit.d { "ApiRepositoryImpl getRatesViaBackend $base" }
    }

    override suspend fun getRatesViaApi(base: String) = apiRequest {
        if (base.isEmpty()) throw EmptyParameterException()
        else apiFactory.getRatesViaApi(base).toModel(base)
    }.also {
        kermit.d { "ApiRepositoryImpl getRatesViaApi $base" }
    }
}

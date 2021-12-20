/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api.repo

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.api.service.ApiService
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.model.EmptyParameterException
import com.github.mustafaozhan.ccc.common.platformCoroutineContext
import com.github.mustafaozhan.ccc.common.util.Result
import kotlinx.coroutines.withContext

internal class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {

    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> apiRequest(
        suspendBlock: suspend () -> T
    ) = withContext(platformCoroutineContext) {
        try {
            Result.Success(suspendBlock.invoke())
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }

    override suspend fun getRatesByBackend(base: String) = apiRequest {
        apiService.getRatesByBackend(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getRatesByBackend $base" }
    }

    override suspend fun getRatesByAPI(base: String) = apiRequest {
        apiService.getRatesByAPI(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getRatesByAPI $base" }
    }

    override suspend fun getRatesByPremiumAPI(base: String) = apiRequest {
        apiService.getRatesByPremiumAPI(
            base.ifEmpty { throw EmptyParameterException() }
        ).toModel(base)
    }.also {
        Logger.v { "ApiRepositoryImpl getRatesByPremiumAPI $base" }
    }
}

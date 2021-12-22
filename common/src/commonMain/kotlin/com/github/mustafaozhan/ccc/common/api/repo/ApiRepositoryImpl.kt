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
        Logger.v { "ApiRepositoryImpl getRatesByBackend $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            apiService.getRatesByBackend(base).toModel(base)
        }
    }

    override suspend fun getRatesByAPI(base: String) = apiRequest {
        Logger.v { "ApiRepositoryImpl getRatesByAPI $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            apiService.getRatesByAPI(base).toModel(base)
        }
    }

    override suspend fun getRatesByPremiumAPI(base: String) = apiRequest {
        Logger.v { "ApiRepositoryImpl getRatesByPremiumAPI $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            apiService.getRatesByPremiumAPI(base).toModel(base)
        }
    }
}

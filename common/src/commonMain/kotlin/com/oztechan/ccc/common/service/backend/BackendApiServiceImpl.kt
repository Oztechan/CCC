package com.oztechan.ccc.common.service.backend

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.backend.BackendApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.BaseNetworkService
import kotlinx.coroutines.CoroutineDispatcher

class BackendApiServiceImpl(
    private val backendApi: BackendApi,
    ioDispatcher: CoroutineDispatcher
) : BackendApiService, BaseNetworkService(ioDispatcher) {
    override suspend fun getRates(
        base: String
    ) = apiRequest {
        Logger.v { "BackendApiServiceImpl getRates $base" }
        backendApi.getRates(base.withEmptyParameterCheck()).toModel(base)
    }
}

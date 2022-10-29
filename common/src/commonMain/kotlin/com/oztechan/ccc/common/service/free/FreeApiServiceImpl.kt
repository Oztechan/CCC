package com.oztechan.ccc.common.service.free

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.BaseNetworkService
import kotlinx.coroutines.CoroutineDispatcher

internal class FreeApiServiceImpl(
    private val freeApi: FreeApi,
    ioDispatcher: CoroutineDispatcher
) : FreeApiService, BaseNetworkService(ioDispatcher) {
    override suspend fun getRates(
        base: String
    ) = apiRequest {
        Logger.v { "FreeApiServiceImpl getRates $base" }
        freeApi.getRates(base.withEmptyParameterCheck()).toModel(base)
    }
}

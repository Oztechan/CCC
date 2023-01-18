package com.oztechan.ccc.common.service.free

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.mapper.toExchangeRateModel
import com.oztechan.ccc.common.service.BaseNetworkService
import kotlinx.coroutines.CoroutineDispatcher

internal class FreeApiServiceImpl(
    private val freeApi: FreeApi,
    ioDispatcher: CoroutineDispatcher
) : FreeApiService, BaseNetworkService(ioDispatcher) {
    override suspend fun getConversion(
        base: String
    ) = apiRequest {
        Logger.v { "FreeApiServiceImpl getConversion $base" }
        freeApi.getConversion(base.withEmptyParameterCheck()).toExchangeRateModel(base)
    }
}

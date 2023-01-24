package com.oztechan.ccc.backend.service.free

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.network.api.free.FreeApi
import com.oztechan.ccc.common.core.network.base.BaseNetworkService
import com.oztechan.ccc.common.core.network.mapper.toExchangeRateModel
import kotlinx.coroutines.CoroutineDispatcher

internal class FreeApiServiceImpl(
    private val freeApi: FreeApi,
    ioDispatcher: CoroutineDispatcher
) : FreeApiService, BaseNetworkService(ioDispatcher) {
    override suspend fun getConversion(
        base: String
    ) = apiRequest {
        Logger.v { "FreeApiServiceImpl getConversion $base" }
        freeApi.getConversion(withEmptyParameterCheck(base)).toExchangeRateModel(base)
    }
}

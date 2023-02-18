package com.oztechan.ccc.client.service.backend

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.network.api.backend.BackendApi
import com.oztechan.ccc.common.core.network.base.BaseNetworkService
import com.oztechan.ccc.common.core.network.mapper.toConversionModel
import kotlinx.coroutines.CoroutineDispatcher

internal class BackendApiServiceImpl(
    private val backendApi: BackendApi,
    ioDispatcher: CoroutineDispatcher
) : BackendApiService, BaseNetworkService(ioDispatcher) {
    override suspend fun getConversion(
        base: String
    ) = apiRequest {
        Logger.v { "BackendApiServiceImpl getConversion $base" }

        backendApi.getExchangeRate(withEmptyParameterCheck(base))
            .toConversionModel()
    }
}

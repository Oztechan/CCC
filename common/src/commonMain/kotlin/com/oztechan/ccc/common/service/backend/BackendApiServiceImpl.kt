package com.oztechan.ccc.common.service.backend

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.backend.BackendApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BackendApiServiceImpl(
    private val backendApi: BackendApi,
    private val ioDispatcher: CoroutineDispatcher
) : BackendApiService {
    override suspend fun getRates(
        base: String
    ) = withContext(ioDispatcher) {
        Logger.v { "BackendApiServiceImpl getRates $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            backendApi.getRates(base).toModel(base)
        }
    }
}

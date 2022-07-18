package com.oztechan.ccc.common.service.free

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FreeApiServiceImpl(
    private val freeApi: FreeApi,
    private val ioDispatcher: CoroutineDispatcher
) : FreeApiService {
    override suspend fun getRates(
        base: String
    ) = withContext(ioDispatcher) {
        Logger.v { "FreeApiServiceImpl getRates $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            freeApi.getRates(base).toModel(base)
        }
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.service.premium

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.premium.PremiumApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.BaseNetworkService
import kotlinx.coroutines.CoroutineDispatcher

internal class PremiumApiServiceImpl(
    private val premiumAPI: PremiumApi,
    ioDispatcher: CoroutineDispatcher
) : PremiumApiService, BaseNetworkService(ioDispatcher) {

    override suspend fun getRates(
        base: String
    ) = apiRequest {
        Logger.v { "PremiumApiServiceImpl getRates $base" }
        premiumAPI.getRates(base.withEmptyParameterCheck()).toModel(base)
    }
}

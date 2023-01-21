/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.service.premium

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.network.api.premium.PremiumApi
import com.oztechan.ccc.common.core.network.base.BaseNetworkService
import com.oztechan.ccc.common.core.network.mapper.toExchangeRateModel
import kotlinx.coroutines.CoroutineDispatcher

internal class PremiumApiServiceImpl(
    private val premiumAPI: PremiumApi,
    ioDispatcher: CoroutineDispatcher
) : PremiumApiService, BaseNetworkService(ioDispatcher) {

    override suspend fun getConversion(
        base: String
    ) = apiRequest {
        Logger.v { "PremiumApiServiceImpl getConversion $base" }
        premiumAPI.getConversion(base.withEmptyParameterCheck()).toExchangeRateModel(base)
    }
}

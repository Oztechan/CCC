/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.service.premium

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.api.premium.PremiumApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class PremiumApiServiceImpl(
    private val premiumAPI: PremiumApi,
    private val ioDispatcher: CoroutineDispatcher
) : PremiumApiService {

    override suspend fun getRates(
        base: String
    ) = withContext(ioDispatcher) {
        Logger.v { "PremiumApiServiceImpl getRates $base" }

        if (base.isEmpty()) {
            throw EmptyParameterException()
        } else {
            premiumAPI.getRates(base).toModel(base)
        }
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponse

internal interface ApiService {
    suspend fun getRatesViaApi(base: String): CurrencyResponse
    suspend fun getRatesViaBackend(base: String): CurrencyResponse
}

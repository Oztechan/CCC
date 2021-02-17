/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity

interface ApiService {
    suspend fun getRatesByBaseViaApi(base: String): CurrencyResponseEntity
    suspend fun getRatesByBaseViaBackend(base: String): CurrencyResponseEntity
}

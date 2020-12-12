/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.data.api

import com.github.mustafaozhan.ccc.common.data.api.entity.CurrencyResponseEntity

interface ApiService {
    suspend fun getRatesByBaseViaApi(base: String): CurrencyResponseEntity
    suspend fun getRatesByBaseViaBackend(base: String): CurrencyResponseEntity
}

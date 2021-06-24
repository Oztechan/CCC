/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity

internal interface ApiService {
    suspend fun getRatesViaApi(base: String): CurrencyResponseEntity
    suspend fun getRatesViaBackend(base: String): CurrencyResponseEntity
}

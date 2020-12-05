/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse

interface ApiService {
    suspend fun getRatesByBase(base: String): CurrencyResponse
}

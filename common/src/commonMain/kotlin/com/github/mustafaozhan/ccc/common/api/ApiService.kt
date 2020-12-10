/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity

interface ApiService {
    suspend fun getRatesByBase(base: String): CurrencyResponseEntity
}

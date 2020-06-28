/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.api

import com.github.mustafaozhan.data.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("byBase")
    suspend fun getRatesByBase(@Query("base") base: String): CurrencyResponse
}

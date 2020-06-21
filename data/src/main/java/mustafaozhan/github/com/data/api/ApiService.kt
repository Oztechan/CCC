/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.api

import mustafaozhan.github.com.data.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("byBase")
    suspend fun getRatesByBase(@Query("base") base: String): CurrencyResponse
}

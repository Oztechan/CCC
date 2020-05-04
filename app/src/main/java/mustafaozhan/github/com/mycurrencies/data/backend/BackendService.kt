/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.backend

import kotlinx.coroutines.flow.Flow
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendService {
    @GET("byBase")
    fun getAllOnBase(@Query("base") base: String): Flow<CurrencyResponse>
}

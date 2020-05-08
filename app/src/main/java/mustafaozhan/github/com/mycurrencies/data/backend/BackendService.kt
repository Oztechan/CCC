/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.backend

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendService {
    @GET("byBase")
    fun getRatesByBase(@Query("base") base: String): Observable<CurrencyResponse>
}

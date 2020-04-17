package mustafaozhan.github.com.mycurrencies.data.backend

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface BackendService {
    @GET("byBase")
    fun getAllOnBase(@Query("base") base: String):
        Observable<CurrencyResponse>
}

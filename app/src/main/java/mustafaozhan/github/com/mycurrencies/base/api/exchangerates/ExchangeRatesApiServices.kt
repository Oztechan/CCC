package mustafaozhan.github.com.mycurrencies.base.api.exchangerates

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface ExchangeRatesApiServices {
    @GET("/base")
    fun getAllEvents(): Observable<Any>
}
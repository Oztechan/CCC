package mustafaozhan.github.com.mycurrencies.base.api.exchangerates

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import retrofit2.http.GET

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface ExchangeRatesApiServices {
    @GET("latest")
    fun getAllCurrencies(): Observable<CurrencyResponse>
}
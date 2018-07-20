package mustafaozhan.github.com.mycurrencies.base.api.exchangerates

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface ExchangeRatesApiServices {
    @GET("latest")
    fun getAllOnBase(@Query("base") base: Currencies):
            Observable<CurrencyResponse>

    @GET("latest")
    fun getAllOnBaseAndLimitWith(@Query("baseCurrency") base: String, @Query("symbols") symbols: ArrayList<Currencies>):
            Observable<CurrencyResponse>

}
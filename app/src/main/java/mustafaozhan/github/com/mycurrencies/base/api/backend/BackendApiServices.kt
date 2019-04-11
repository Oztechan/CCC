package mustafaozhan.github.com.mycurrencies.base.api.backend

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface BackendApiServices {
    @GET("{base}")
    fun getAllOnBase(@Path("base") base: Currencies):
        Observable<CurrencyResponse>
}
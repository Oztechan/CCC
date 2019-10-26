package mustafaozhan.github.com.mycurrencies.data.repository

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.data.api.ApiHelper
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository @Inject
constructor(private val apiHelper: ApiHelper) {
    fun getAllOnBase(base: Currencies): Observable<CurrencyResponse> =
        apiHelper.apiService.getAllOnBase(base)
}

package mustafaozhan.github.com.mycurrencies.data.repository

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.data.backend.BackendHelper
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository @Inject
constructor(private val backendHelper: BackendHelper) {
    fun getAllOnBase(base: Currencies): Observable<CurrencyResponse> =
        backendHelper.backendService.getAllOnBase(base)
}

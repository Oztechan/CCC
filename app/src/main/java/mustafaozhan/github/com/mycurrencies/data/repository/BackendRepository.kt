package mustafaozhan.github.com.mycurrencies.data.repository

import mustafaozhan.github.com.mycurrencies.data.backend.BackendHelper
import mustafaozhan.github.com.mycurrencies.model.Currencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository @Inject
constructor(private val backendHelper: BackendHelper) {
    suspend fun getAllOnBase(base: Currencies) = backendHelper.apiRequest {
        backendHelper.backendService.getAllOnBase(base)
    }
}

package mustafaozhan.github.com.mycurrencies.data.repository

import mustafaozhan.github.com.mycurrencies.base.api.BaseApiRepository
import mustafaozhan.github.com.mycurrencies.data.backend.BackendHelper
import mustafaozhan.github.com.mycurrencies.model.Currencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository @Inject
constructor(private val backendHelper: BackendHelper) : BaseApiRepository() {
    suspend fun getAllOnBase(base: Currencies) = apiRequest {
        backendHelper.backendService.getAllOnBase(base)
    }
}

package mustafaozhan.github.com.mycurrencies.data.backend

import mustafaozhan.github.com.mycurrencies.base.api.BaseApiRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository @Inject
constructor(override val apiHelper: BackendHelper) : BaseApiRepository() {

    suspend fun getAllOnBase(base: Currencies) = apiRequest {
        apiHelper.backendService.getAllOnBase(base)
    }
}

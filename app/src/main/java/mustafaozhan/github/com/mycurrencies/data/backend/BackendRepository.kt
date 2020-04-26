package mustafaozhan.github.com.mycurrencies.data.backend

import com.github.mustafaozhan.basemob.api.BaseApiRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository
@Inject constructor(
    override val apiHelper: BackendHelper
) : BaseApiRepository() {

    suspend fun getAllOnBase(base: String) = apiRequest {
        apiHelper.backendService.getAllOnBase(base)
    }

    suspend fun getAllOnBaseLongTimeOut(base: String) = apiRequest {
        apiHelper.backendServiceLongTimeOut.getAllOnBase(base)
    }
}

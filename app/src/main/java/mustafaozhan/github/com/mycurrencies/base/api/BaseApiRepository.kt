package mustafaozhan.github.com.mycurrencies.base.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseApiRepository {

    suspend fun <T> apiRequest(suspendBlock: suspend () -> T) =
        withContext(Dispatchers.IO) {
            suspendBlock.invoke()
        }
}

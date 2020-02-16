package mustafaozhan.github.com.mycurrencies.base.api

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mustafaozhan.github.com.mycurrencies.error.ModelMappingException
import mustafaozhan.github.com.mycurrencies.error.NetworkException
import mustafaozhan.github.com.mycurrencies.error.UnknownNetworkException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

@Suppress("ThrowsCount", "TooGenericExceptionCaught")
abstract class BaseApiRepository {

    abstract val apiHelper: BaseApiHelper

    suspend fun <T> apiRequest(suspendBlock: suspend () -> T) =
        withContext(Dispatchers.IO) {
            try {
                suspendBlock.invoke()
            } catch (e: Throwable) {
                when (e) {
                    is CancellationException -> throw e
                    is UnknownHostException,
                    is TimeoutException,
                    is SSLException -> throw NetworkException(e)
                    is JsonDataException -> throw ModelMappingException(e)
//                    is HttpException -> throw
                    else -> throw UnknownNetworkException(e)
                }
            }
        }
}

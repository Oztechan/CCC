package mustafaozhan.github.com.mycurrencies.error

import retrofit2.Response

@Suppress("UNUSED_PARAMETER")
class RetrofitException(
    message: String?,
    url: String?,
    response: Response<*>,
    cause: Throwable
) : Throwable(cause)

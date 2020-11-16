/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.api

import com.github.mustafaozhan.data.error.EmptyParameterException
import com.github.mustafaozhan.data.error.InternetConnectionException
import com.github.mustafaozhan.data.error.ModelMappingException
import com.github.mustafaozhan.data.error.NetworkException
import com.github.mustafaozhan.data.error.RetrofitException
import com.github.mustafaozhan.data.error.UnknownNetworkException
import com.github.mustafaozhan.data.model.CurrencyType
import com.github.mustafaozhan.data.model.NullBaseException
import com.github.mustafaozhan.data.model.Result
import com.squareup.moshi.JsonDataException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@Singleton
class ApiRepository
@Inject constructor(private val apiFactory: ApiFactory) {

    @Suppress("ThrowsCount", "TooGenericExceptionCaught")
    suspend fun <T> apiRequest(suspendBlock: suspend () -> T) =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(suspendBlock.invoke())
            } catch (e: Throwable) {
                Result.Error(
                    when (e) {
                        is CancellationException -> e
                        is UnknownHostException,
                        is TimeoutException,
                        is IOException,
                        is SSLException -> NetworkException(e)
                        is ConnectException -> InternetConnectionException(e)
                        is JsonDataException -> ModelMappingException(e)
                        is HttpException -> RetrofitException(
                            e.response()?.code().toString() + " " + e.response()?.message(),
                            e.response(),
                            e
                        )
                        else -> UnknownNetworkException(e)
                    }
                )
            }
        }

    suspend fun getRatesByBase(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == CurrencyType.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.apiService.getRatesByBase(base)
        }
    }
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.error

import retrofit2.Response

open class NetworkException(cause: Throwable) : Throwable(cause)

class UnknownNetworkException(cause: Throwable) : NetworkException(cause)

class ModelMappingException(cause: Throwable) : NetworkException(cause)

class InternetConnectionException(cause: Throwable) : NetworkException(cause)

class EmptyParameterException : NetworkException(Exception("parameter can not be empty"))

@Suppress("UNUSED_PARAMETER", "unused")
class RetrofitException(
    override val message: String?,
    val response: Response<*>?,
    override val cause: Throwable
) : NetworkException(cause)
